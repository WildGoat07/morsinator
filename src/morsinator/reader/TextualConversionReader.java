package morsinator.reader;

import java.io.*;
import java.util.*;

import morsinator.collections.MorsiBinaryTree;
import morsinator.collections.MorsiList;
import morsinator.reader.ConversionReaderException;

public class TextualConversionReader implements ConversionReader {
    private int readReader(Reader reader, int row) throws ConversionReaderException {
        try {
            return reader.read();
        } catch(IOException e) {
            throw new ConversionReaderException("Erreur de lecture du fichier", row, e);
        }
    }

    @Override
    public void fill(Reader reader, MorsiList<ConversionRow> tm, MorsiBinaryTree<String, Character> mt) throws ConversionReaderException {
        int row = 1;
        HashSet<String> addedLetters = new HashSet<>();
        tm.clear();

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
                    value = value.trim();
                    tm.add(new ConversionRow(key.charAt(0), value));

                    if(mt.get(value) != null)
                        throw new ConversionReaderException("Code morse de '" + key + "' déjà ajouté sous la lettre '" + mt.get(value) + "'", row);

                    try {
                        mt.set(value, key.charAt(0));
                    } catch(RuntimeException e) {
                        throw new ConversionReaderException(row, e);
                    }

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
    }
}
