package morsinator.reader;

import java.io.*;

import morsinator.collections.*;
import morsinator.collections.generics.MorsiBinaryTree;

public interface ConversionReader {
    /**
     * Remplis les collections de conversions à partir d'un flux textuel
     * 
     * @param reader text d'entrée d'entrée
     * @param tm     liste de conversion texte -> morse
     * @param mt     arbre de conversion morse -> texte
     * @throws IOException
     */
    public void fill(Reader reader, ConversionList tm, MorsiBinaryTree<String, Character> mt) throws ConversionReaderException;
}
