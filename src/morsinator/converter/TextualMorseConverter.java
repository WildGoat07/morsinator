package morsinator.converter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import morsinator.collections.MorseConversion;
import morsinator.collections.TextConversion;

public class TextualMorseConverter implements MorseConverter {
    private enum Step {
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
        Step step = Step.WAITING_FOR_TOKEN;
        try {
            while ((current = reader.read()) != -1) {
                switch (step) {
                    case WAITING_FOR_TOKEN:
                        if (!isCharIgnored((char) current)) {
                            if (!firstWord)
                                writer.write(" / ");
                            firstWord = false;
                            writer.write(textConversion.getMorse((char) current));
                            step = Step.WORD_PARSING;
                        }
                        break;
                    case WORD_PARSING:
                        if (isCharIgnored((char) current))
                            step = Step.WAITING_FOR_TOKEN;
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

    @Override
    public void morseToText(Reader reader, Writer writer, MorseConversion morseConversion) {
        // TODO Auto-generated method stub

    }

}
