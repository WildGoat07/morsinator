package morsinator;

import morsinator.reader.ConversionRow;
import morsinator.reader.ConversionReader;
import morsinator.reader.TextualConversionReader;
import morsinator.reader.BinaryConversionReader;
import morsinator.collections.MorsiList;
import morsinator.collections.MorsiBinaryTree;
import morsinator.reader.ConversionReaderException;

import java.io.*;

public class Morsinator {
    public static void main(String[] args) {
        if(args.length != 5) {
            System.err.println("morsinator <option-lecteur-table> <option-conversion> <table-conversion> <fichier-entrée> <fichier-sortie>\n\n" +
                               "Options de lecteur de table :\n" +
                               "    -txt  --text-table-reader    Utilise le lecteur textuel de table de conversion\n" +
                               "    -bin  --bin-table-reader     Utilise le lecteur binaire de table de conversion\n\n" +
                               "Options de conversion :\n" +
                               "    -tm   --texte-morse          Convertit de texte vers morse\n" +
                               "    -mt   --morse-texte          Convertit de morse vers texte");
            System.exit(1);
        }

        ConversionReader conversionReader = null;

        if(args[0].equals("-txt") || args[0].equals("--text-table-reader")) {
            conversionReader = new TextualConversionReader();
        } else if(args[0].equals("-bin") || args[0].equals("--bin-table-reader")) {
            conversionReader = new BinaryConversionReader();
        } else {
            System.err.println("L'option " + args[0] + " est inconnue");
            System.exit(1);
        }

        boolean morseToText;

        if(args[1].equals("-tm") || args[1].equals("--texte-morse")) {
            morseToText = false;
        } else if(args[1].equals("-mt") || args[1].equals("--morse-texte")) {
            morseToText = true;
        } else {
            System.err.println("L'option " + args[1] + " est inconnue");
            System.exit(1);
        }

        FileInputStream conversionFile = null;

        try {
            conversionFile = new FileInputStream(args[2]);
        } catch(FileNotFoundException exception) {
            System.err.println("Table de conversion introuvable");
            System.exit(1);
        }

        MorsiList<ConversionRow> morsiList = new MorsiList<ConversionRow>();
        MorsiBinaryTree<String, Character> morsiBinaryTree = new MorsiBinaryTree<String, Character>(MorsiBinaryTree.morseConvert);

        try {
            conversionReader.fill(new InputStreamReader(new BufferedInputStream(conversionFile)), morsiList,
                    morsiBinaryTree);
            conversionFile.close();
        } catch (ConversionReaderException exception) {
            System.err.println("Erreur de lecture de la table de conversion\n" + args[2] + ":" + exception.getRow()
                    + " : " + exception.getMessage());
            System.exit(1);
        } catch (IOException exception) {
            System.err.println("Erreur de fermeture du fichier\n" + exception.getMessage());
            System.exit(1);
        }
    }
}