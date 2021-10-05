package morsinator.interfaces;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import morsinator.MorsinatorParseException;
import morsinator.collections.ConversionBinaryTree;
import morsinator.collections.ConversionList;
import morsinator.collections.MorseConversion;
import morsinator.collections.TextConversion;
import morsinator.converter.MorseConverter;
import morsinator.converter.TextualMorseConverter;
import morsinator.reader.ConversionReader;
import morsinator.reader.TextualConversionReader;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.application.Platform;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.stream.Collectors;

import javafx.fxml.FXML;

public class MainWindowController {
    @FXML
    private TextArea textCodeArea;
    @FXML
    private TextArea morseCodeArea;

    private Stage stage;

    private MorseConverter morseConverter;
    private TextConversion textConversion;
    private MorseConversion morseConversion;
    private File lastText;
    private File lastMorse;
    private Thread conversionThread;
    private Object mutex;

    public MainWindowController(Stage stage) {
        this.stage = stage;
        lastText = null;
        lastMorse = null;
        conversionThread = null;
        morseConverter = new TextualMorseConverter();
        textConversion = new ConversionList();
        morseConversion = new ConversionBinaryTree();
        initConversionTable();
    }

    public void textToMorse() {
        if (conversionThread != null)
            conversionThread.interrupt();
        final String text = textCodeArea.getText();
        conversionThread = new Thread(() -> {
            Reader reader = new StringReader(text);
            Writer writer = new StringWriter();
            try {
                morseConverter.textToMorse(reader, writer, textConversion);
            } catch (MorsinatorParseException e) {
                // TODO
            }
            final String morse = writer.toString();
            Platform.runLater(() -> {
                morseCodeArea.setText(morse);
                conversionThread = null;
            });
        });
        conversionThread.start();
    }

    public void morseToText() {
        if (conversionThread != null)
            conversionThread.interrupt();
        final String morse = morseCodeArea.getText();
        conversionThread = new Thread(() -> {
            Reader reader = new StringReader(morse);
            Writer writer = new StringWriter();
            try {
                morseConverter.morseToText(reader, writer, morseConversion);
            } catch (MorsinatorParseException e) {
                // TODO
            }
            final String text = writer.toString();
            Platform.runLater(() -> {
                textCodeArea.setText(text);
                conversionThread = null;
            });
        });
        conversionThread.start();
    }

    private void initConversionTable() {
        InputStream conversionFile = null;
        try {
            conversionFile = new FileInputStream("./conversions.txt");
        } catch (FileNotFoundException e) {
            // TODO fichier introuvable
            System.exit(1);
        }
        ConversionReader conversionReader = new TextualConversionReader();

        try {
            conversionReader.fill(new InputStreamReader(new BufferedInputStream(conversionFile)), textConversion,
                    morseConversion);
            conversionFile.close();
        } catch (MorsinatorParseException exception) {
            // TODO erreur de fichier
            System.exit(1);
        } catch (IOException exception) {
        }
    }

    public void importText() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer du texte");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fichiers texte", "*.txt"),
                new ExtensionFilter("Tous les fichiers", "*.*"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Reader reader = new BufferedReader(new FileReader(file));
                StringBuilder builder = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1)
                    builder.append((char) character);
                reader.close();
                textCodeArea.setText(builder.toString());
                lastText = file;
                textToMorse();
            } catch (FileNotFoundException e) {
                // TODO fichier introuvable
            } catch (IOException e) {
            }
        }
    }

    public void importMorse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer du morse");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fichiers texte", "*.txt"),
                new ExtensionFilter("Tous les fichiers", "*.*"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Reader reader = new BufferedReader(new FileReader(file));
                StringBuilder builder = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1)
                    builder.append((char) character);
                reader.close();
                morseCodeArea.setText(builder.toString());
                lastMorse = file;
                morseToText();
            } catch (FileNotFoundException e) {
                // TODO fichier introuvable
            } catch (IOException e) {
            }
        }
    }

    public void saveText() {
        if (lastText == null)
            saveTextAs();
        else {
            try {
                Writer writer = new FileWriter(lastText);
                writer.append(textCodeArea.getText());
                writer.close();
            } catch (IOException e) {
                // TODO
            }
        }
    }

    public void saveTextAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder du texte");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fichiers texte", "*.txt"),
                new ExtensionFilter("Tous les fichiers", "*.*"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            lastText = file;
            saveText();
        }
    }

    public void saveMorse() {
        if (lastMorse == null)
            saveMorseAs();
        else {
            try {
                Writer writer = new FileWriter(lastMorse);
                writer.append(morseCodeArea.getText());
                writer.close();
            } catch (IOException e) {
                // TODO
            }
        }
    }

    public void saveMorseAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder du morse");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fichiers texte", "*.txt"),
                new ExtensionFilter("Tous les fichiers", "*.*"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            lastMorse = file;
            saveMorse();
        }
    }
}