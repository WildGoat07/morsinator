package morsinator.reader;

import java.io.Reader;
import java.io.IOException;

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

    public void fill(Reader reader, MorsiList<ConversionRow> list, MorsiBinaryTree<String, Character> tree) throws ConversionReaderException {
        int c;
        state = State.READ_LETTER;
        int row = 1;

        try {
            c = reader.read();
        } catch(IOException exception) {
            throw new ConversionReaderException("Erreur de lecture du fichier", row);
        }

        while(c != -1) {
            if(c == '\n') {
                row++;
            }

            switch(state) {
                case READ_LETTER:
                    if(c >= 'A' && c <= 'Z') {
                        curRow = new ConversionRow();
                        curRow.letter = (char)c;
                        state = State.READ_EQUAL;
                    } else if(c != ' ' && c != '\n' && c != '\t') {
                        throw new ConversionReaderException("Lettre invalide", row);
                    }

                    break;

                case READ_EQUAL:
                    if(c == '=') {
                        state = State.READ_FIRST_MORSE_CHAR;
                    } else if(c != ' ' && c != '\n' && c != '\t') {
                        throw new ConversionReaderException("Égal attendu", row);
                    }

                    break;

                case READ_FIRST_MORSE_CHAR:
                    if(c == '.' || c == '_') {
                        morseBuilder = new StringBuilder("" + c);
                        state = State.READ_MORSE_SEQUENCE;
                    } else if(c != ' ' && c != '\n' && c != '\t') {
                        throw new ConversionReaderException("Caractère morse invalide", row);
                    }

                    break;

                case READ_MORSE_SEQUENCE:
                    if(c == '.' || c == '_') {
                        morseBuilder.append(c);
                    } else if(c == ' ' || c == '\n' || c == '\t') {
                        state = State.READ_LETTER;
                        curRow.morse = morseBuilder.toString();
                        list.add(curRow);
                    } else {
                        throw new ConversionReaderException("Caractère morse invalide", row);
                    }

                    break;
            }

            try {
                c = reader.read();
            } catch(IOException exception) {
                throw new ConversionReaderException("Erreur de lecture du fichier", row);
            }
        }

        if(state != State.READ_LETTER) {
            throw new ConversionReaderException("Fin de fichier inattendue", row);
        }
    }
}