package morsinator.reader;

import java.io.*;

import morsinator.collections.MorsiList;

public interface ConversionReader {
    /**
     * Remplis les collections de conversions à partir d'un flux textuel
     * 
     * @param stream flux txetuel d'entrée
     * @param tm     liste de conversion texte -> morse
     * @throws IOException
     */
    // TODO supporter l'abre binaire de déconversion
    public void fill(InputStream stream, MorsiList<ConversionRow> tm);
}
