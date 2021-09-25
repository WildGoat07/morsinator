package morsinator.reader;

import java.io.InputStream;
import java.io.IOException;

import morsinator.reader.ConversionRow;
import morsinator.reader.ConversionReader;
import morsinator.reader.ConversionReaderException;
import morsinator.collections.MorsiBinaryTree;
import morsinator.collections.MorsiList;

public class BinaryConversionReader implements ConversionReader {
    private enum State {
        READ_LETTER,
        READ_EQUAL,
        READ_FIRST_MORSE_CHAR,
        READ_MORSE_SEQUENCE
    }

    private State state;
    private ConversionRow curRow;
    private StringBuilder morseBuilder;

    public void fill(InputStream stream, MorsiList<ConversionRow> list, MorsiBinaryTree<String, Character> tree) {
        byte[] buf = new byte[1024];
        state = State.READ_LETTER;
        int bufLen;
        int row = 1;

        try {
            bufLen = stream.read(buf);
        } catch(IOException exception) {
            throw new ConversionReaderException("Erreur de lecture du fichier", row);
        }

        while(bufLen != -1) {
            for(int i = 0; i < bufLen; i++) {
                byte b = buf[i];

                if(b == '\n') {
                    row++;
                }

                switch(state) {
                    case READ_LETTER:
                        if(b >= 'A' && b <= 'Z') {
                            curRow = new ConversionRow();
                            curRow.letter = (char)b;
                            state = State.READ_EQUAL;
                        } else if(b != ' ' && b != '\n' && b != '\t') {
                            throw new ConversionReaderException("Lettre invalide", row);
                        }

                        break;

                    case READ_EQUAL:
                        if(b == '=') {
                            state = State.READ_FIRST_MORSE_CHAR;
                        } else if(b != ' ' && b != '\n' && b != '\t') {
                            throw new ConversionReaderException("Égal attendu", row);
                        }

                        break;

                    case READ_FIRST_MORSE_CHAR:
                        if(b == '.' || b == '_') {
                            morseBuilder = new StringBuilder("" + (char)b);
                            state = State.READ_MORSE_SEQUENCE;
                        } else if(b != ' ' && b != '\n' && b != '\t') {
                            throw new ConversionReaderException("Caractère morse invalide", row);
                        }

                        break;

                    case READ_MORSE_SEQUENCE:
                        if(b == '.' || b == '_') {
                            morseBuilder.append((char)b);
                        } else if(b == ' ' || b == '\n' || b == '\t') {
                            state = State.READ_LETTER;
                            curRow.morse = morseBuilder.toString();
                            list.add(curRow);
                        } else {
                            throw new ConversionReaderException("Caractère morse invalide", row);
                        }

                        break;
                }
            }

            try {
                bufLen = stream.read(buf);
            } catch(IOException exception) {
                throw new ConversionReaderException("Erreur de lecture du fichier", row);
            }
        }
    }
}