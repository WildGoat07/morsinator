package morsinator.converter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import morsinator.collections.MorseConversion;
import morsinator.collections.TextConversion;

public class TextualMorseConverter implements MorseConverter {
    private enum TextStep {
        WORD_PARSING, WAITING_FOR_TOKEN
    }

    private static final char[] ignoredChars = new char[] { '\n', '\r', ' ' };

    private static boolean isCharIgnored(char toTest) {
        for (int i = 0; i < ignoredChars.length; ++i)
            if (ignoredChars[i] == toTest)
                return true;
        return false;
    }

    @Override
    public void textToMorse(Reader reader, Writer writer, TextConversion textConversion) {
        int current;
        boolean firstWord = true;
        TextStep step = TextStep.WAITING_FOR_TOKEN;
        try {
            while ((current = reader.read()) != -1) {
                switch (step) {
                    case WAITING_FOR_TOKEN:
                        if (!isCharIgnored((char) current)) {
                            if (!firstWord)
                                writer.write(" / ");
                            firstWord = false;
                            writer.write(textConversion.getMorse((char) current));
                            step = TextStep.WORD_PARSING;
                        }
                        break;
                    case WORD_PARSING:
                        if (isCharIgnored((char) current))
                            step = TextStep.WAITING_FOR_TOKEN;
                        else
                            writer.write(' ' + textConversion.getMorse((char) current));
                        break;
                    default:
                        break;

                }
            }
        } catch (IOException e) {

        }
    }

    private int readReader(Reader reader) {
        try {
            return reader.read();
        } catch(IOException e) {
            // throw new CustomException()...
            return -1;
        }
    }

    private void writeWriter(Writer writer, char c) {
        try {
            writer.write(c);
        } catch(IOException e) {
            // throw new CustomException()...
        }
    }

    private enum MorseStep {
        READ_MORSE,
        WAIT_NEXT,
        WAIT_SPACE
    }

    @Override
    public void morseToText(Reader reader, Writer writer, MorseConversion morseConversion) {
        int current;
        MorseStep step = MorseStep.READ_MORSE;
        String morse = "";

        while((current = readReader(reader)) != -1) {
            char c = (char)current;

            switch(step) {
                case READ_MORSE:
                    if(c == ' ') {
                        writeWriter(writer, morseConversion.getLetter(morse));
                        step = MorseStep.WAIT_NEXT;
                    } else {
                        morse += c;
                    }

                    break;

                case WAIT_NEXT:
                    if(c == '/') {
                        writeWriter(writer, ' ');
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
