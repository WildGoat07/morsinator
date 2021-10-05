package morsinator.converter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import morsinator.MorsinatorParseException;
import morsinator.collections.MorseConversion;
import morsinator.collections.TextConversion;

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

    private int readReader(Reader reader) throws MorsinatorParseException {
        try {
            return reader.read();
        } catch(IOException e) {
            throw new MorsinatorParseException("Erreur de lecture du flux");
        }
    }

    private void writeWriter(Writer writer, char c) throws MorsinatorParseException {
        try {
            writer.write(c);
        } catch(IOException e) {
            throw new MorsinatorParseException("Erreur d'écriture dans le flux");
        }
    }

    private void writeWriter(Writer writer, String s) throws MorsinatorParseException {
        try {
            writer.write(s);
        } catch(IOException e) {
            throw new MorsinatorParseException("Erreur d'écriture dans le flux");
        }
    }

    private static boolean isCharIgnored(char toTest) {
        for (int i = 0; i < ignoredChars.length; ++i)
            if (ignoredChars[i] == toTest)
                return true;
        return false;
    }

    @Override
    public void textToMorse(Reader reader, Writer writer, TextConversion textConversion) throws MorsinatorParseException {
        int current;
        boolean firstWord = true;
        TextStep step = TextStep.WAITING_FOR_TOKEN;

        while ((current = readReader(reader)) != -1) {
            switch (step) {
                case WAITING_FOR_TOKEN:
                    if (!isCharIgnored((char) current)) {
                        if (!firstWord)
                            writeWriter(writer, " / ");
                        firstWord = false;
                        writeWriter(writer, textConversion.getMorse((char) current));
                        step = TextStep.WORD_PARSING;
                    }
                    break;
                case WORD_PARSING:
                    if (isCharIgnored((char) current))
                        step = TextStep.WAITING_FOR_TOKEN;
                    else
                        writeWriter(writer, ' ' + textConversion.getMorse((char) current));
                    break;
                default:
                    break;

            }
        }
    }

    @Override
    public void morseToText(Reader reader, Writer writer, MorseConversion morseConversion) throws MorsinatorParseException {
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
