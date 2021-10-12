package morsinator.interfaces;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import morsinator.collections.MorseConversion;
import morsinator.collections.TextConversion;
import morsinator.table.ConversionWriter;
import morsinator.table.TextualConversion;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.geometry.Pos;

public class EditTableController {
    private class TableRow {
        public char letter;
        public String morse;

        public TableRow(char letter, String morse) {
            this.letter = letter;
            this.morse = morse;
        }
    }

    @FXML
    private TextField addLetter;
    @FXML
    private TextField addMorse;
    @FXML
    private TableView rowsList;
    @FXML
    private TableColumn letterColumn;
    @FXML
    private TableColumn morseColumn;
    @FXML
    private TableColumn deleteColumn;

    private Stage stage;
    private TextConversion textConversion;
    private MorseConversion morseConversion;

    private static Image removeRowImage;

    public EditTableController(Stage stage, TextConversion tm, MorseConversion mt) {
        this.stage = stage;
        textConversion = tm;
        morseConversion = mt;
    }

    public void load() {
        if (removeRowImage == null)
            removeRowImage = new Image(
                    getClass().getClassLoader().getResource("assets/icons/RemoveRow_16x.png").toString());
        List<Entry<Character, String>> rows = new ArrayList<>(textConversion.getRows());
        Collections.sort(rows, new Comparator<Entry<Character, String>>() {
            @Override
            public int compare(Entry<Character, String> o1, Entry<Character, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        letterColumn
                .setCellValueFactory(new Callback<CellDataFeatures<TableRow, Character>, ObservableValue<Character>>() {
                    public ObservableValue<Character> call(CellDataFeatures<TableRow, Character> row) {
                        return new ReadOnlyObjectWrapper(row.getValue().letter);
                    }
                });
        morseColumn.setCellValueFactory(new Callback<CellDataFeatures<TableRow, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<TableRow, String> row) {
                return new ReadOnlyObjectWrapper(row.getValue().morse);
            }
        });
        deleteColumn.setCellFactory(new Callback<TableColumn<TableRow, Void>, TableCell<TableRow, Void>>() {
            @Override
            public TableCell<TableRow, Void> call(TableColumn<TableRow, Void> arg0) {
                return new TableCell<TableRow, Void>() {

                    private Button button = new Button();
                    {
                        var iv = new ImageView();
                        iv.setImage(removeRowImage);
                        button.setGraphic(iv);
                        alignmentProperty().set(Pos.BASELINE_CENTER);
                        button.setOnAction((ActionEvent event) -> {
                            char letter = ((TableRow) rowsList.getItems().get(getIndex())).letter;
                            String morse = ((TableRow) rowsList.getItems().get(getIndex())).morse;
                            textConversion.removeRow(letter, morse);
                            morseConversion.removeRow(letter, morse);
                            rowsList.getItems().remove(getIndex(), getIndex() + 1);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            };
        });
        rowsList.setPlaceholder(new Label("Aucune information de conversion"));
        for (Entry<Character, String> row : rows)
            pushRow(row.getKey(), row.getValue());
    }

    public void pushRow(Character letter, String morse) {
        rowsList.getItems().add(new TableRow(letter, morse));
    }

    public void addRow() {
        try {
            if (addLetter.getText().trim().length() != 1)
                throw new IllegalArgumentException("Un seul caractère est autorisé par lettre");
            if (addMorse.getText().trim().length() == 0)
                throw new IllegalArgumentException("Le code morse ne peut pas être vide");
            char letter = Character.toUpperCase(addLetter.getText().trim().charAt(0));
            String morse = addMorse.getText().trim();
            for (Entry<Character, String> row : textConversion.getRows())
                if (row.getKey().equals(letter))
                    throw new IllegalArgumentException("La lettre a déjà été ajoutée");
                else if (row.getValue().equals(morse))
                    throw new IllegalArgumentException("Le code morse a déjà été ajouté");
            morseConversion.addRow(letter, morse);
            textConversion.addRow(letter, morse);
            pushRow(letter, morse);
            addLetter.setText("");
            addMorse.setText("");
        } catch (IllegalArgumentException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("Impossible d'ajouter une ligne");
            dialog.setContentText(e.getMessage());
            dialog.showAndWait();
        }
    }

    public void close() {
        stage.close();
    }

    public void exportTable() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter la table");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fichiers texte", "*.txt"),
                new ExtensionFilter("Tous les fichiers", "*.*"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                Writer writer = new FileWriter(file);
                ConversionWriter conversionWriter = new TextualConversion();
                conversionWriter.save(writer, textConversion);
                writer.close();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("Impossible d'exporter la table");
                dialog.setContentText(e.getMessage());
                dialog.showAndWait();
            }
        }
    }
}
