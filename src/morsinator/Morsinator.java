package morsinator;

import morsinator.collections.*;
import morsinator.converter.*;
import morsinator.interfaces.MainWindowController;
import morsinator.table.*;
import morsinator.text.TextPosition;

import java.io.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;

public class Morsinator extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("morsinator/interfaces/MainWindow.fxml"));
        fxmlLoader.setController(new MainWindowController(stage));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Morsinator");
        stage.setMinHeight(400);
        stage.setMinWidth(500);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource("assets/icons/icon.png").toString()));
        stage.show();
    }

    private static void printHelpAndExit() {
        System.err.println("morsinator <option-conversion> <table-conversion> <fichier-entrée> <fichier-sortie>\n\n"
                + "Options :\n" + "    -tm  --texte-morse    Convertit de texte vers morse\n"
                + "    -mt  --morse-texte    Convertit de morse vers texte");
        System.exit(1);
    }

    private static boolean parseOption(String[] args) {
        if (args[0].equals("-tm") || args[0].equals("--texte-morse")) {
            return false;
        } else if (args[0].equals("-mt") || args[0].equals("--morse-texte")) {
            return true;
        } else {
            System.err.println("L'option " + args[0] + " est inconnue");
            System.exit(1);
            return false;
        }
    }

    private static InputStream getConversionFileStream(String[] args) {
        try {
            return new FileInputStream(args[1]);
        } catch (FileNotFoundException e) {
            System.err.println("Table de conversion introuvable");
            System.exit(1);
        }

        return null;
    }

    private static void getConversionCollections(String[] args, TextConversion textConversion,
            MorseConversion morseConversion) {
        InputStream conversionFile = getConversionFileStream(args);
        ConversionReader conversionReader = new TextualConversion();

        try {
            conversionReader.fill(new InputStreamReader(new BufferedInputStream(conversionFile)), textConversion,
                    morseConversion);
        } catch (MorsinatorParseException e) {
            TextPosition tp = e.getTextPos();
            System.err.println("Erreur de lecture de la table de conversion\n" + args[1] + ":" + tp.getRow()
                    + ":" + tp.getCol() + " : " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Erreur de lecture de la table de conversion\n" + e.getMessage());
            System.exit(1);
        }

        try {
            conversionFile.close();
        } catch (IOException e) {
            System.err.println("Erreur de fermeture de la table de conversion");
            System.exit(1);
        }
    }

    private static InputStream openInputStream(String[] args) {
        try {
            return new FileInputStream(args[2]);
        } catch (FileNotFoundException e) {
            System.err.println("Fichier d'entrée introuvable");
            System.exit(1);
            return null;
        }
    }

    private static OutputStream openOutputStream(String[] args) {
        try {
            return new FileOutputStream(args[3]);
        } catch (FileNotFoundException e) {
            System.err.println("Fichier de sortie introuvable");
            System.exit(1);
            return null;
        }
    }

    private static Reader openReader(String[] args) {
        InputStream inputStream = openInputStream(args);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        return new InputStreamReader(bufferedInputStream);
    }

    private static Writer openWriter(String[] args) {
        OutputStream outputStream = openOutputStream(args);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        return new OutputStreamWriter(bufferedOutputStream);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            launch();
            return;
        } else if (args.length != 4) {
            printHelpAndExit();
        }

        boolean morseToText = parseOption(args);
        TextConversion textConversion = new ConversionList();
        MorseConversion morseConversion = new ConversionBinaryTree();

        getConversionCollections(args, textConversion, morseConversion);
        Reader reader = openReader(args);
        Writer writer = openWriter(args);

        MorseConverter morseConverter = new TextualMorseConverter();

        if (morseToText) {
            try {
                morseConverter.morseToText(reader, writer, morseConversion);
            } catch (MorsinatorParseException e) {
                TextPosition tp = e.getTextPos();
                System.err.println("Erreur de traduction du fichier morse\n" + args[2] + ":" + tp.getRow()
                        + ":" + tp.getCol() + " : " + e.getMessage());
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Erreur d'entrée-sortie à la conversion");
                System.exit(1);
            }
        } else {
            try {
                morseConverter.textToMorse(reader, writer, textConversion);
            } catch (MorsinatorParseException e) {
                TextPosition tp = e.getTextPos();
                System.err.println("Erreur de traduction du fichier texte\n" + args[2] + ":" + tp.getRow()
                        + ":" + tp.getCol() + " : " + e.getMessage());
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Erreur d'entrée-sortie à la conversion");
                System.exit(1);
            }
        }

        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Erreur à la fermeture du flux d'écriture dans le fichier de sortie");
            System.exit(1);
        }

        System.exit(0);
    }
}