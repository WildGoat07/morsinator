package morsinator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import morsinator.reader.ConversionRow;

public class ConversionReader {
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

    public ConversionReader(String path) throws FileNotFoundException {
        stream = new FileInputStream(path);
        rows = new ArrayList<>();
    }

    public void read() throws IOException {
        byte[] buf = new byte[1024];
        state = State.READ_LETTER;
        int bufLen = stream.read(buf);

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
                            System.err.println("Lettre invalide");
                            System.exit(1);
                        }

                        break;

                    case READ_EQUAL:
                        if(b == '=') {
                            state = State.READ_FIRST_MORSE_CHAR;
                        } else if(b != ' ' && b != '\n' && b != '\t') {
                            System.err.println("Égal attendu");
                            System.exit(1);
                        }

                        break;

                    case READ_FIRST_MORSE_CHAR:
                        if(b == '.' || b == '_') {
                            morseBuilder = new StringBuilder("" + (char)b);
                            state = State.READ_MORSE_SEQUENCE;
                        } else if(b != ' ' && b != '\n' && b != '\t') {
                            System.err.println("Caractère morse invalide");
                            System.exit(1);
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
                            System.err.println("Caractère morse invalide");
                            System.exit(1);
                        }

                        break;
                }
            }

            bufLen = stream.read(buf);
        }
    }
}