package morsinator.reader;

import java.io.*;
import java.util.*;

import morsinator.MorsinatorParseException;
import morsinator.collections.*;
import morsinator.text.ReaderTextPos;
import morsinator.text.TextPosition;

public class TextualConversionReader implements ConversionReader {
    private void registerRow(String key, String value, TextConversion tm, MorseConversion mt, TextPosition textPos) throws MorsinatorParseException {
        value = value.trim();
        tm.addRow(key.charAt(0), value);

        try {
            mt.addRow(key.charAt(0), value);
        } catch(IllegalArgumentException e) {
            throw new MorsinatorParseException(e.getMessage(), textPos);
        }
    }

    @Override
    public void fill(Reader reader, TextConversion tm, MorseConversion mt) throws MorsinatorParseException, IOException {
        ReaderTextPos readerTp = new ReaderTextPos(reader);
        HashSet<String> addedLetters = new HashSet<>();

        String key = "";
        String value = null;
        boolean readingKey = true; // if false, reading value
        int currentChar = readerTp.read();

        while(currentChar != -1) {
            if(readingKey) {
                // étape 1 : lecture de la clé (la lettre)
                if(currentChar == '=') {
                    key = key.trim().toUpperCase();

                    // vérifications d'intégrités
                    if(key.length() != 1)
                        throw new MorsinatorParseException("Lettre invalide", readerTp.getTextPos());
                    else if(addedLetters.contains(key))
                        throw new MorsinatorParseException("Lettre déjà ajoutée", readerTp.getTextPos());

                    addedLetters.add(key);
                    readingKey = false;
                    value = "";
                } else {
                    key += (char) currentChar;
                }
            } else {
                // étape 2 : lecture de la valeur (le code morse)
                if(currentChar == '\n' && !value.trim().isEmpty()) {
                    registerRow(key, value, tm, mt, readerTp.getTextPos());
                    readingKey = true;
                    key = "";
                } else {
                    value += (char) currentChar;
                }
            }

            currentChar = readerTp.read();
        }

        if(readingKey && !key.trim().isEmpty())
            throw new MorsinatorParseException("Fin de fichier inattendue", readerTp.getTextPos());
        else if(!readingKey) {
            registerRow(key, value, tm, mt, readerTp.getTextPos());
        }
    }
}
