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

    @Override
    public void textToMorse(Reader reader, Writer writer, TextConversion textConversion) {
        int current;
        boolean firstWord = true;
        Step step = Step.WAITING_FOR_TOKEN;
        try {
            while ((current = reader.read()) != -1) {
                switch (step) {
                    case WAITING_FOR_TOKEN:
                        if ((char) current == '\n') {
                            writer.write('\n');
                            firstWord = true;
                        } else if ((char) current != ' ' && (char) current != '\r') {
                            if (!firstWord)
                                writer.write(" / ");
                            firstWord = false;
                            writer.write(textConversion.getMorse((char) current));
                            step = Step.WORD_PARSING;
                        }
                        break;
                    case WORD_PARSING:
                        if ((char) current == ' ')
                            step = Step.WAITING_FOR_TOKEN;
                        else if ((char) current == '\n') {
                            writer.write('\n');
                            firstWord = true;
                            step = Step.WAITING_FOR_TOKEN;
                        } else if ((char) current != '\r')
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
