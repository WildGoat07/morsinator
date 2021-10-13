package morsinator.table;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import morsinator.MorsinatorParseException;
import morsinator.collections.*;
import morsinator.text.ReaderTextPos;
import morsinator.text.TextPosition;

public class TextualConversion implements ConversionReader, ConversionWriter {
    private enum Step {
        READ_KEY,
        ESCAPE_KEY_CHAR,
        READ_VALUE
    }

    private void registerRow(String key, String value, TextConversion tm, MorseConversion mt, TextPosition textPos)
            throws MorsinatorParseException {
        value = value.trim();

        try {
            tm.addRow(key.charAt(0), value);
            mt.addRow(key.charAt(0), value);
        } catch (IllegalArgumentException e) {
            throw new MorsinatorParseException(e.getMessage(), textPos);
        }
    }

    @Override
    public void fill(Reader reader, TextConversion tm, MorseConversion mt)
            throws MorsinatorParseException, IOException {
        ReaderTextPos readerTp = new ReaderTextPos(reader);

        String key = "";
        String value = null;
        Step step = Step.READ_KEY;
        int readRes;

        while ((readRes = readerTp.read()) != -1) {
            char c = (char)readRes;

            if (step == Step.READ_KEY) {
                // étape 1 : lecture de la clé (la lettre)
                if (c == '\\') {
                    // échappement de caractère (pour le "=" par exemple)
                    step = Step.ESCAPE_KEY_CHAR;
                } else if (c == '=') {
                    key = key.trim().toUpperCase();

                    // vérifications d'intégrités
                    if (key.length() != 1)
                        throw new MorsinatorParseException("Lettre invalide", readerTp.getTextPos());

                    step = Step.READ_VALUE;
                    value = "";
                } else {
                    key += c;
                }
            } else if(step == Step.ESCAPE_KEY_CHAR) {
                key += c;
                step = Step.READ_KEY;
            } else {
                // étape 2 : lecture de la valeur (le code morse)
                if (c == '\n' && !value.trim().isEmpty()) {
                    registerRow(key, value, tm, mt, readerTp.getTextPos());
                    step = Step.READ_KEY;
                    key = "";
                } else {
                    value += c;
                }
            }
        }

        if(step == Step.READ_VALUE)
            registerRow(key, value, tm, mt, readerTp.getTextPos());
        else if(!key.trim().isEmpty())
            throw new MorsinatorParseException("Fin de fichier inattendue", readerTp.getTextPos());
    }

    @Override
    public void save(Writer writer, TextConversion tm) throws IOException {
        for (Entry<Character, String> entry : tm.getRows()) {
            if (entry.getKey().equals('='))
                writer.write('\\');
            writer.write(entry.getKey());
            writer.write(" = ");
            writer.write(entry.getValue());
            writer.write('\n');
        }
    }
}
