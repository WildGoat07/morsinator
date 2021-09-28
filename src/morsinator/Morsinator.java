package morsinator;

import morsinator.reader.ConversionRow;
import morsinator.reader.ConversionReader;
import morsinator.reader.TextualConversionReader;
import morsinator.collections.ConversionList;
import morsinator.collections.MorsiBinaryTree;
import morsinator.reader.ConversionReaderException;

import java.io.*;

public class Morsinator {
    public static void main(String[] args) {
        if(args.length != 4) {
            System.err.println("morsinator <option-conversion> <table-conversion> <fichier-entrée> <fichier-sortie>\n\n" +
                               "Options :\n" +
                               "    -tm   --texte-morse          Convertit de texte vers morse\n" +
                               "    -mt   --morse-texte          Convertit de morse vers texte");
            System.exit(1);
        }

        boolean morseToText;

        if(args[0].equals("-tm") || args[0].equals("--texte-morse")) {
            morseToText = false;
        } else if(args[0].equals("-mt") || args[0].equals("--morse-texte")) {
            morseToText = true;
        } else {
            System.err.println("L'option " + args[0] + " est inconnue");
            System.exit(1);
        }

        FileInputStream conversionFile = null;

        try {
            conversionFile = new FileInputStream(args[1]);
        } catch(FileNotFoundException exception) {
            System.err.println("Table de conversion introuvable");
            System.exit(1);
        }

        ConversionReader conversionReader = new TextualConversionReader();
        ConversionList conversionList = new ConversionList();
        MorsiBinaryTree<String, Character> morsiBinaryTree = new MorsiBinaryTree<String, Character>(MorsiBinaryTree.morseConvert);

        try {
            conversionReader.fill(new InputStreamReader(new BufferedInputStream(conversionFile)), conversionList,
                    morsiBinaryTree);
            conversionFile.close();
        } catch (ConversionReaderException exception) {
            System.err.println("Erreur de lecture de la table de conversion\n" + args[1] + ":" + exception.getRow()
                    + " : " + exception.getMessage());
            System.exit(1);
        } catch (IOException exception) {
            System.err.println("Erreur de fermeture du fichier\n" + exception.getMessage());
            System.exit(1);
        }
    }
}