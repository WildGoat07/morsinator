package morsinator.converter;

import java.io.*;

public interface MorseConverter {
    /**
     * Converti une source de texte en morse
     * 
     * @param reader source textuelle à convertir
     * @param writer cible en morse
     */
    public void textToMorse(Reader reader, Writer writer);

    /**
     * Converti du code morse en text
     * 
     * @param reader source en morse
     * @param writer cible en texte
     */
    public void morseToText(Reader reader, Writer writer);
}
