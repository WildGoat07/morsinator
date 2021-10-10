package morsinator.converter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import morsinator.MorsinatorParseException;
import morsinator.collections.MorseConversion;
import morsinator.collections.TextConversion;
import morsinator.reader.ReaderRowCol;

public class TextualMorseConverter implements MorseConverter {
    private static final char[] ignoredChars = new char[] { '\n', '\r', ' ' };

    private enum MorseStep {
        READ_MORSE,
        WAIT_NEXT,
        WAIT_SPACE
    }

    private enum TextStep {
        WORD_PARSING, WAITING_FOR_TOKEN
    }

    private static boolean isCharIgnored(char toTest) {
        for (int i = 0; i < ignoredChars.length; ++i)
            if (ignoredChars[i] == toTest)
                return true;
        return false;
    }

    @Override
    public void textToMorse(Reader reader, Writer writer, TextConversion textConversion) throws MorsinatorParseException, IOException {
        ReaderRowCol readerRc = new ReaderRowCol(reader);
        int current;
        boolean firstWord = true;
        TextStep step = TextStep.WAITING_FOR_TOKEN;

        while ((current = readerRc.read()) != -1) {
            switch (step) {
                case WAITING_FOR_TOKEN:
                    if (!isCharIgnored((char) current)) {
                        if (!firstWord)
                            writer.write(" / ");
                        firstWord = false;

                        String morse;

                        try {
                            morse = textConversion.getMorse((char) current);
                        } catch(IllegalArgumentException e) {
                            throw new MorsinatorParseException(e.getMessage(), readerRc.getRow());
                        }

                        writer.write(morse);
                        step = TextStep.WORD_PARSING;
                    }
                    break;
                case WORD_PARSING:
                    if (isCharIgnored((char) current))
                        step = TextStep.WAITING_FOR_TOKEN;
                    else {
                        String morse = null;

                        try {
                            morse = textConversion.getMorse((char) current);
                        } catch(IllegalArgumentException e) {
                            throw new MorsinatorParseException(e.getMessage(), readerRc.getRow());
                        }

                        writer.write(' ' + morse);
                    }

                    break;
                default:
                    break;

            }
        }
    }

    @Override
    public void morseToText(Reader reader, Writer writer, MorseConversion morseConversion) throws MorsinatorParseException, IOException {
        ReaderRowCol readerRc = new ReaderRowCol(reader);
        int current;
        MorseStep step = MorseStep.READ_MORSE;
        String morse = "";

        while((current = readerRc.read()) != -1) {
            char c = (char)current;

            switch(step) {
                case READ_MORSE:
                    if(c == ' ') {
                        char letter;

                        try {
                            letter = morseConversion.getLetter(morse);
                        } catch(IllegalArgumentException e) {
                            throw new MorsinatorParseException(e.getMessage(), readerRc.getRow());
                        }

                        writer.write(letter);
                        step = MorseStep.WAIT_NEXT;
                    } else {
                        morse += c;
                    }

                    break;

                case WAIT_NEXT:
                    if(c == '/') {
                        writer.write(' ');
                        step = MorseStep.WAIT_SPACE;
                    } else {
                        morse = "" + c;
                        step = MorseStep.READ_MORSE;
                    }

                    break;

                case WAIT_SPACE:
                    if(c == ' ') {
                        morse = "";
                        step = MorseStep.READ_MORSE;
                    }

                    break;
            }
        }
    }
}
