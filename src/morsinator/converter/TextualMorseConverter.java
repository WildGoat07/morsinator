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

    private static boolean isCharIgnored(char toTest) {
        for (int i = 0; i < ignoredChars.length; ++i)
            if (ignoredChars[i] == toTest)
                return true;
        return false;
    }

    @Override
    public void textToMorse(Reader reader, Writer writer, TextConversion textConversion) throws MorsinatorParseException, IOException {
        ReaderTextPos readerTp = new ReaderTextPos(reader);
        int readRes;
        boolean inWord = false;
        boolean firstWord = true;

        while ((readRes = readerTp.read()) != -1) {
            char c = (char)readRes;

            if(isCharIgnored(c)) {
                inWord = false;
            } else {
                if(inWord) {
                    writer.write(' ');
                } else {
                    inWord = true;

                    if(firstWord) {
                        firstWord = false;
                    } else {
                        writer.write(" / ");
                    }
                }

                String morse;

                try {
                    morse = textConversion.getMorse(c);
                } catch(IllegalArgumentException e) {
                    throw new MorsinatorParseException(e.getMessage(), readerTp.getTextPos());
                }

                writer.write(morse);
            }
        }
    }

    @Override
    public void morseToText(Reader reader, Writer writer, MorseConversion morseConversion) throws MorsinatorParseException, IOException {
        ReaderTextPos readerTp = new ReaderTextPos(reader);
        int readRes;
        boolean readingMorse = false;
        String morse = "";

        while((readRes = readerTp.read()) != -1) {
            char c = (char)readRes;

            if(readingMorse) {
                if (c == ' ' || c == '/') {
                    char letter;

                    try {
                        letter = morseConversion.getLetter(morse);
                    } catch(IllegalArgumentException e) {
                        throw new MorsinatorParseException(e.getMessage(), readerTp.getTextPos());
                    }

                    writer.write(letter);

                    if(c == '/') {
                        writer.write(' ');
                    }

                    readingMorse = false;
                } else {
                    morse += c;
                }
            } else {
                if(c == '/') {
                    writer.write(' ');
                } else if (!isCharIgnored(c)) {
                    morse = "" + c;
                    readingMorse = true;
                }
            }
        }

        if(readingMorse) {
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
