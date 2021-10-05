package morsinator.converter;

import java.io.*;

import morsinator.MorsinatorParseException;
import morsinator.collections.MorseConversion;
import morsinator.collections.TextConversion;

public interface MorseConverter {
    /**
     * Convertit une source de texte en morse
     * 
     * @param reader         source textuelle à convertir
     * @param writer         cible en morse
     * @param textConversion converteur de texte
     */
    public void textToMorse(Reader reader, Writer writer, TextConversion textConversion) throws MorsinatorParseException, IOException;

    /**
     * Convertit du code morse en text
     * 
     * @param reader          source en morse
     * @param writer          cible en texte
     * @param morseConversion converteur de morse
     */
    public void morseToText(Reader reader, Writer writer, MorseConversion morseConversion) throws MorsinatorParseException, IOException;
}
