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
    /**
     * Liste des caractères à ignorer lors de la traduction morse
     */
    private static final char[] ignoredChars = new char[] { '\n', '\r', ' ' };

    private static boolean isCharIgnored(char toTest) {
        for (int i = 0; i < ignoredChars.length; ++i)
            if (ignoredChars[i] == toTest)
                return true;
        return false;
    }

    @Override
    public void textToMorse(Reader reader, Writer writer, TextConversion textConversion)
            throws MorsinatorParseException, IOException {
        ReaderTextPos readerTp = new ReaderTextPos(reader);
        int readRes; // résultat de la lecture du Reader
        boolean inWord = false; // vrai si on est en train d'analyse un mot
        boolean firstWord = true; // permet de ne pas gérer les espaces avant le premier mot

        while ((readRes = readerTp.read()) != -1) { // tant que on est pas à la fin
            char c = (char) readRes;

            if (isCharIgnored(c)) {
                // si le caractère est ignoré, c'est qu'on est en dehors d'un mot
                inWord = false;
            } else {
                if (inWord) {
                    // dans un mot, on ajoute une nouvelle lettre
                    writer.write(' ');
                } else { // sinon on débute un nouveau mot
                    inWord = true;
                    if (firstWord) {
                        firstWord = false;
                    } else {
                        // si on est après le premier mot, on sépare le morse
                        writer.write(" / ");
                    }
                }

                try {
                    // on écrit le morse dans le buffer
                    writer.write(textConversion.getMorse(c));
                } catch (IllegalArgumentException e) {
                    // erreur de lettre inconnue
                    throw new MorsinatorParseException(e.getMessage(), readerTp.getTextPos());
                }
            }
        }
    }

    @Override
    public void morseToText(Reader reader, Writer writer, MorseConversion morseConversion)
            throws MorsinatorParseException, IOException {
        ReaderTextPos readerTp = new ReaderTextPos(reader);
        int readRes; // résultat de la lecture du Reader
        boolean readingMorse = false; // vrai si on lit un caractère morse
        String morse = ""; // buffer du caractère morse

        while ((readRes = readerTp.read()) != -1) { // tant qu'on est pas à la fin du fichier
            char c = (char) readRes;

            if (readingMorse) {
                // si on lit déjà une lettre morse
                if (isCharIgnored(c) || c == '/') {
                    // si on termine une lettre morse
                    try {
                        // on écrit l'ancien buffer en texte classique
                        writer.write(morseConversion.getLetter(morse));
                    } catch (IllegalArgumentException e) {
                        // erreur de code morse inconnu
                        throw new MorsinatorParseException(e.getMessage(), readerTp.getTextPos());
                    }

                    if (c == '/') {
                        // si on change de mot, on ajoute un espace
                        writer.write(' ');
                    }

                    readingMorse = false; // on viens de finir un mot, on le signale
                } else {
                    // sinon, on ajoute le code morse au buffer de lettre
                    morse += c;
                }
            } else {
                // si on ne lit pas de lettre morse
                if (c == '/') {
                    // séparateur de mot, on ajoute un espace
                    writer.write(' ');
                } else if (!isCharIgnored(c)) {
                    // si on commence un mot
                    morse = Character.toString(c);
                    readingMorse = true;
                }
            }
        }

        if (readingMorse) {
            // si on était en train de lire une lettre morse à la fin du fichier
            try {
                // on ajoute cette dernière lettre
                writer.write(morseConversion.getLetter(morse));
            } catch (IllegalArgumentException e) {
                // erreur de code morse inconnu
                throw new MorsinatorParseException(e.getMessage(), readerTp.getTextPos());
            }
        }
    }
}
