package morsinator.interfaces;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import morsinator.Morsinator;
import morsinator.MorsinatorParseException;
import morsinator.collections.ConversionBinaryTree;
import morsinator.collections.ConversionList;
import morsinator.collections.MorseConversion;
import morsinator.collections.TextConversion;
import morsinator.converter.MorseConverter;
import morsinator.converter.TextualMorseConverter;
import morsinator.table.ConversionReader;
import morsinator.table.TextualConversion;
import morsinator.text.TextPosition;

import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.Parent;

import javafx.application.Platform;

import javafx.fxml.FXMLLoader;

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
import java.nio.charset.Charset;
import java.util.stream.Collectors;

import javafx.fxml.FXML;

public class MainWindowController {
    @FXML
    private TextArea textCodeArea;
    @FXML
    private TextArea morseCodeArea;
    @FXML
    private Label textErrorLabel;
    @FXML
    private Label morseErrorLabel;
    @FXML
    private Circle textErrorCircle;
    @FXML
    private Circle morseErrorCircle;

    private Stage stage;

    private MorseConverter morseConverter;
    private TextConversion textConversion;
    private MorseConversion morseConversion;
    private File lastText;
    private File lastMorse;
    private Thread conversionThread;
    private Object mutex;
    private File lastTable;
    private boolean lastEditedText;

    public MainWindowController(Stage stage) {
        this.stage = stage;
        lastText = null;
        lastMorse = null;
        lastTable = new File("./conversions.txt");
        conversionThread = null;
        morseConverter = new TextualMorseConverter();
        textConversion = new ConversionList();
        morseConversion = new ConversionBinaryTree();
        initConversionTable();
    }

    public void textToMorse() {
        lastEditedText = true;
        if (conversionThread != null)
            conversionThread.interrupt();
        final String text = textCodeArea.getText();
        conversionThread = new Thread(() -> {
            lastEditedText = true;
            Reader reader = new StringReader(text);
            Writer writer = new StringWriter();
            try {
                morseConverter.textToMorse(reader, writer, textConversion);
                final String morse = writer.toString();
                Platform.runLater(() -> {
                    morseCodeArea.setText(morse);
                    conversionThread = null;
                    textErrorLabel.setText("");
                    textErrorCircle.setFill(Color.LIME);
                    morseErrorLabel.setText("");
                    morseErrorCircle.setFill(Color.LIME);
                });
            } catch (MorsinatorParseException e) {
                Platform.runLater(() -> {
                    TextPosition tp = e.getTextPos();
                    textErrorLabel.setText("Position " + tp.getPos() + " : " + e.getMessage());
                    textErrorCircle.setFill(Color.RED);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    textErrorLabel.setText(e.getMessage());
                    textErrorCircle.setFill(Color.RED);
                });
            }
        });
        conversionThread.start();
    }

    public void morseToText() {
        if (conversionThread != null)
            conversionThread.interrupt();
        final String morse = morseCodeArea.getText();
        conversionThread = new Thread(() -> {
            lastEditedText = false;
            Reader reader = new StringReader(morse);
            Writer writer = new StringWriter();
            try {
                morseConverter.morseToText(reader, writer, morseConversion);
                final String text = writer.toString();
                Platform.runLater(() -> {
                    textCodeArea.setText(text);
                    conversionThread = null;
                    textErrorLabel.setText("");
                    textErrorCircle.setFill(Color.LIME);
                    morseErrorLabel.setText("");
                    morseErrorCircle.setFill(Color.LIME);
                });
            } catch (MorsinatorParseException e) {
                Platform.runLater(() -> {
                    TextPosition tp = e.getTextPos();
                    morseErrorLabel.setText("Position " + tp.getPos() + " : " + e.getMessage());
                    morseErrorCircle.setFill(Color.RED);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    morseErrorLabel.setText(e.getMessage());
                    morseErrorCircle.setFill(Color.RED);
                });
            }
        });
        conversionThread.start();
    }

    private void initConversionTable() {
        try {
            InputStream conversionFile = new FileInputStream(lastTable);
            ConversionReader conversionReader = new TextualConversion();
            textConversion.clear();
            morseConversion.clear();
            conversionReader.fill(
                    new InputStreamReader(new BufferedInputStream(conversionFile), Charset.forName("UTF-8")),
                    textConversion, morseConversion);
            conversionFile.close();
        } catch (FileNotFoundException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("Erreur de lecture de la table de conversion");
            dialog.setContentText("Fichier de table de conversion '" + lastTable.getAbsolutePath() + "' introuvable.");
            dialog.showAndWait();
        } catch (MorsinatorParseException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            TextPosition tp = e.getTextPos();

            dialog.setTitle("Erreur de lecture de la table de conversion");
            dialog.setContentText("Ligne " + tp.getRow() + " " + e.getMessage());
            dialog.showAndWait();
        } catch (IOException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("Erreur de lecture de la table de conversion");
            dialog.setContentText(e.getMessage());
            dialog.showAndWait();
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
                Reader reader = new BufferedReader(new FileReader(file, Charset.forName("UTF-8")));
                StringBuilder builder = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1)
                    builder.append((char) character);
                reader.close();
                textCodeArea.setText(builder.toString());
                lastText = file;
                textToMorse();
            } catch (FileNotFoundException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("Erreur de d'import de fichier");
                dialog.setContentText("Fichier introuvable.");
                dialog.showAndWait();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("Erreur de d'import de fichier");
                dialog.setContentText(e.getMessage());
                dialog.showAndWait();
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
                Reader reader = new BufferedReader(new FileReader(file, Charset.forName("UTF-8")));
                StringBuilder builder = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1)
                    builder.append((char) character);
                reader.close();
                morseCodeArea.setText(builder.toString());
                lastMorse = file;
                morseToText();
            } catch (FileNotFoundException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("Erreur de d'import de fichier");
                dialog.setContentText("Fichier introuvable.");
                dialog.showAndWait();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("Erreur de d'import de fichier");
                dialog.setContentText(e.getMessage());
                dialog.showAndWait();
            }
        }
    }

    public void close() {
        stage.close();
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
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("Erreur de d'export de fichier");
                dialog.setContentText(e.getMessage());
                dialog.showAndWait();
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
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("Erreur de d'export de fichier");
                dialog.setContentText(e.getMessage());
                dialog.showAndWait();
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

    public void importTable() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer une table");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fichiers texte", "*.txt"),
                new ExtensionFilter("Tous les fichiers", "*.*"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            lastTable = file;
            refreshTable();
        }
    }

    public void editTable() {
        try {
            Stage tableStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getClassLoader().getResource("morsinator/interfaces/EditTable.fxml"));
            EditTableController controller = new EditTableController(tableStage, textConversion, morseConversion);
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            controller.load();
            Scene scene = new Scene(root, 600, 700);

            tableStage.setTitle("Gestion de la table");
            tableStage.setMinHeight(600);
            tableStage.setMinWidth(500);
            tableStage.setScene(scene);
            tableStage.getIcons()
                    .add(new Image(getClass().getClassLoader().getResource("assets/icons/icon.png").toString()));
            tableStage.initModality(Modality.WINDOW_MODAL);
            tableStage.initOwner(stage);
            tableStage.showAndWait();
            if (lastEditedText)
                textToMorse();
            else
                morseToText();
        } catch (IOException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("Erreur interne");
            dialog.setContentText(e.getMessage());
            dialog.showAndWait();
        }
    }

    public void refreshTable() {
        initConversionTable();
        if (lastEditedText)
            textToMorse();
        else
            morseToText();
    }
}