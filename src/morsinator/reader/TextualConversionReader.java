package morsinator.reader;

import java.io.*;
import java.util.*;

import morsinator.collections.MorsiBinaryTree;
import morsinator.collections.MorsiList;

public class TextualConversionReader implements ConversionReader {

    @Override
    public void fill(InputStream stream, MorsiList<ConversionRow> tm, MorsiBinaryTree<String, Character> mt) {
        Reader reader = new InputStreamReader(new BufferedInputStream(stream));
        int row = 1;
        HashSet<String> addedLetters = new HashSet<>();
        tm.clear();
        try {
            while (true) {
                String key = "";
                String value = "";
                int currentChar;
                // étape 1 : lecture de la clé (la lettre)
                while ((currentChar = reader.read()) != -1 && currentChar != '=') {
                    if (currentChar == '\n')
                        ++row;
                    key += (char) currentChar;
                }
                key = key.trim().toUpperCase();
                // si la clé est vide et qu'on est à la fin du fichier, on arrête
                if (currentChar == -1 && key.isEmpty())
                    break;
                // vérifications d'intégrités
                if (currentChar == -1)
                    throw new ConversionReaderException("Fin de fichier inattendue", row);
                if (key.length() != 1)
                    throw new ConversionReaderException("Lettre invalide", row);
                if (addedLetters.contains(key))
                    throw new ConversionReaderException("Lettre déjà ajoutée", row);
                else
                    addedLetters.add(key);
                // étape 2 : lecture de la valeur (le code morse)
                while ((currentChar = reader.read()) != -1 && (currentChar != '\n' || value.trim().isEmpty())) {
                    if (currentChar == '\n')
                        ++row;
                    value += (char) currentChar;
                }
                ++row;
                value = value.trim();
                tm.add(new ConversionRow(key.charAt(0), value));
                // si on est à la fin du fichier, on arrête
                if (currentChar == -1)
                    break;
            }
        } catch (IOException e) {
            throw new ConversionReaderException("Erreur de lecture du fichier", row, e);
        }
    }

}
