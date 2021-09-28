package morsinator.reader;

import java.io.*;
import java.util.*;

import morsinator.collections.*;

public class TextualConversionReader implements ConversionReader {
    private int readReader(Reader reader, int row) throws ConversionReaderException {
        try {
            return reader.read();
        } catch(IOException e) {
            throw new ConversionReaderException("Erreur de lecture du fichier", row, e);
        }
    }

    private void registerRow(String key, String value, TextConversion tm, MorseConversion mt, int row) throws ConversionReaderException {
        value = value.trim();
        tm.addRow(key.charAt(0), value);

        try {
            mt.addRow(key.charAt(0), value);
        } catch(AddException e) {
            throw new ConversionReaderException(row, e);
        }
    }

    @Override
    public void fill(Reader reader, TextConversion tm, MorseConversion mt) throws ConversionReaderException {
        int row = 1;
        HashSet<String> addedLetters = new HashSet<>();

        String key = "";
        String value = null;
        boolean readingKey = true; // if false, reading value
        int currentChar = readReader(reader, row);

        while(currentChar != -1) {
            if(currentChar == '\n')
                ++row;

            if(readingKey) {
                // étape 1 : lecture de la clé (la lettre)
                if(currentChar == '=') {
                    key = key.trim().toUpperCase();

                    // vérifications d'intégrités
                    if(key.length() != 1)
                        throw new ConversionReaderException("Lettre invalide", row);
                    else if(addedLetters.contains(key))
                        throw new ConversionReaderException("Lettre déjà ajoutée", row);

                    addedLetters.add(key);
                    readingKey = false;
                    value = "";
                } else {
                    key += (char) currentChar;
                }
            } else {
                // étape 2 : lecture de la valeur (le code morse)
                if(currentChar == '\n' && !value.trim().isEmpty()) {
                    registerRow(key, value, tm, mt, row);
                    readingKey = true;
                    key = "";
                } else {
                    value += (char) currentChar;
                }
            }

            currentChar = readReader(reader, row);
        }

        if(readingKey && !key.trim().isEmpty())
            throw new ConversionReaderException("Fin de fichier inattendue", row);
        else if(!readingKey) {
            registerRow(key, value, tm, mt, row);
        }
    }
}
