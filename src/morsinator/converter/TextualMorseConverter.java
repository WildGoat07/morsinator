package morsinator.converter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import morsinator.MorsinatorParseException;
import morsinator.collections.MorseConversion;
import morsinator.collections.TextConversion;
import morsinator.text.ReaderTextPos;
import morsinator.text.TextPosition;

public class TextualMorseConverter implements MorseConverter {
    private static final char[] ignoredChars = new char[] { '\n', '\r', ' ' };

    private enum MorseStep {
        WAIT_NEXT,
        READ_MORSE
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
        ReaderTextPos readerTp = new ReaderTextPos(reader);
        int current;
        boolean firstWord = true;
        TextStep step = TextStep.WAITING_FOR_TOKEN;

        while ((current = readerTp.read()) != -1) {
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
                            throw new MorsinatorParseException(e.getMessage(), readerTp.getTextPos());
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
                            throw new MorsinatorParseException(e.getMessage(), readerTp.getTextPos());
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
        ReaderTextPos readerTp = new ReaderTextPos(reader);
        int current;
        MorseStep step = MorseStep.WAIT_NEXT;
        String morse = "";

        while((current = readerTp.read()) != -1) {
            char c = (char)current;

            switch(step) {
                case WAIT_NEXT:
                    if(c == '/') {
                        writer.write(' ');
                    } else if(c != ' ') {
                        morse = "" + c;
                        step = MorseStep.READ_MORSE;
                    }

                    break;

                case READ_MORSE:
                    if(c == ' ') {
                        char letter;

                        try {
                            letter = morseConversion.getLetter(morse);
                        } catch(IllegalArgumentException e) {
                            throw new MorsinatorParseException(e.getMessage(), readerTp.getTextPos());
                        }

                        writer.write(letter);
                        step = MorseStep.WAIT_NEXT;
                    } else if(c == '/') {
                        writer.write(' ');
                        step = MorseStep.WAIT_NEXT;
                    } else {
                        morse += c;
                    }

                    break;
            }
        }

        if(step == MorseStep.READ_MORSE) {
            char letter;

            try {
                letter = morseConversion.getLetter(morse);
            } catch(IllegalArgumentException e) {
                throw new MorsinatorParseException(e.getMessage(), readerTp.getTextPos());
            }

            writer.write(letter);
        }
    }
}
