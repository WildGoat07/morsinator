package morsinator.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import morsinator.reader.ConversionRow;
import morsinator.reader.ConversionReaderException;

public class BinaryConversionReader {
    private enum State {
        READ_LETTER,
        READ_EQUAL,
        READ_FIRST_MORSE_CHAR,
        READ_MORSE_SEQUENCE
    }

    private FileInputStream stream;
    private State state;
    private ConversionRow curRow;
    private StringBuilder morseBuilder;
    private ArrayList<ConversionRow> rows;

    public BinaryConversionReader(String path) throws FileNotFoundException {
        stream = new FileInputStream(path);
        rows = new ArrayList<>();
    }

    public void read() throws IOException {
        byte[] buf = new byte[1024];
        state = State.READ_LETTER;
        int bufLen;

        try {
            bufLen = stream.read(buf);
        } catch(IOException exception) {
            throw new ConversionReaderException("Erreur de lecture du fichier", 0);
        }

        while(bufLen != -1) {
            for(int i = 0; i < bufLen; i++) {
                byte b = buf[i];

                switch(state) {
                    case READ_LETTER:
                        if(b >= 'A' && b <= 'Z') {
                            curRow = new ConversionRow();
                            curRow.letter = (char)b;
                            state = State.READ_EQUAL;
                        } else if(b != ' ' && b != '\n' && b != '\t') {
                            throw new ConversionReaderException("Lettre invalide", 0);
                        }

                        break;

                    case READ_EQUAL:
                        if(b == '=') {
                            state = State.READ_FIRST_MORSE_CHAR;
                        } else if(b != ' ' && b != '\n' && b != '\t') {
                            throw new ConversionReaderException("Égal attendu", 0);
                        }

                        break;

                    case READ_FIRST_MORSE_CHAR:
                        if(b == '.' || b == '_') {
                            morseBuilder = new StringBuilder("" + (char)b);
                            state = State.READ_MORSE_SEQUENCE;
                        } else if(b != ' ' && b != '\n' && b != '\t') {
                            throw new ConversionReaderException("Caractère morse invalide", 0);
                        }

                        break;

                    case READ_MORSE_SEQUENCE:
                        if(b == '.' || b == '_') {
                            morseBuilder.append((char)b);
                        } else if(b == ' ' || b == '\n' || b == '\t') {
                            state = State.READ_LETTER;
                            curRow.morse = morseBuilder.toString();
                            rows.add(curRow);
                        } else {
                            throw new ConversionReaderException("Caractère morse invalide", 0);
                        }

                        break;
                }
            }

            try {
                bufLen = stream.read(buf);
            } catch(IOException exception) {
                throw new ConversionReaderException("Erreur de lecture du fichier", 0);
            }
        }
    }
}