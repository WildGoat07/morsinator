package morsinator.table;

import java.io.IOException;
import java.io.Writer;

import morsinator.collections.TextConversion;

public interface ConversionWriter {
    /**
     * Sauvegarde la table de conversion dans un flux textuel
     * @param writer flux textuel contenant la table
     * @param tm table de conversion à sauvegarder
     * @throws IOException erreur d'écriture dans le flux
     */
    void save(Writer writer, TextConversion tm) throws IOException;
}
