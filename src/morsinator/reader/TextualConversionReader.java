package morsinator.reader;

import java.io.*;
import java.util.*;

import morsinator.MorsinatorParseException;
import morsinator.collections.*;
import morsinator.text.ReaderTextPos;

public class TextualConversionReader implements ConversionReader {
    private void registerRow(String key, String value, TextConversion tm, MorseConversion mt, int row) throws MorsinatorParseException {
        value = value.trim();
        tm.addRow(key.charAt(0), value);

        try {
            mt.addRow(key.charAt(0), value);
        } catch(IllegalArgumentException e) {
            throw new MorsinatorParseException(e.getMessage(), row);
        }
    }

    @Override
    public void fill(Reader reader, TextConversion tm, MorseConversion mt) throws MorsinatorParseException, IOException {
        ReaderTextPos readerTp = new ReaderTextPos(reader);
        HashSet<String> addedLetters = new HashSet<>();

        String key = "";
        String value = null;
        boolean readingKey = true; // if false, reading value
        int currentChar = reader.read();

        while(currentChar != -1) {
            if(readingKey) {
                // étape 1 : lecture de la clé (la lettre)
                if(currentChar == '=') {
                    key = key.trim().toUpperCase();

                    // vérifications d'intégrités
                    if(key.length() != 1)
                        throw new MorsinatorParseException("Lettre invalide", readerTp.getTextPos().getRow());
                    else if(addedLetters.contains(key))
                        throw new MorsinatorParseException("Lettre déjà ajoutée", readerTp.getTextPos().getRow());

                    addedLetters.add(key);
                    readingKey = false;
                    value = "";
                } else {
                    key += (char) currentChar;
                }
            } else {
                // étape 2 : lecture de la valeur (le code morse)
                if(currentChar == '\n' && !value.trim().isEmpty()) {
                    registerRow(key, value, tm, mt, readerTp.getTextPos().getRow());
                    readingKey = true;
                    key = "";
                } else {
                    value += (char) currentChar;
                }
            }

            currentChar = reader.read();
        }

        if(readingKey && !key.trim().isEmpty())
            throw new MorsinatorParseException("Fin de fichier inattendue", readerTp.getTextPos().getRow());
        else if(!readingKey) {
            registerRow(key, value, tm, mt, readerTp.getTextPos().getRow());
        }
    }
}
