package morsinator.reader;

import java.io.*;

import morsinator.collections.*;

public interface ConversionReader {
    /**
     * Remplis les collections de conversions à partir d'un flux textuel
     * 
     * @param stream flux txetuel d'entrée
     * @param tm     liste de conversion texte -> morse
     * @param mt     arbre de conversion morse -> texte
     * @throws IOException
     */
    public void fill(InputStream stream, MorsiList<ConversionRow> tm, MorsiBinaryTree<String, Character> mt);
}
