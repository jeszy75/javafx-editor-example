package editor;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;

public class EditorController {

    @FXML
    private TextArea editor;

    private final EditorModel editorModel = new EditorModel();

    @FXML
    private void initialize() {
        Bindings.bindBidirectional(editor.textProperty(), editorModel.contentProperty());
        Platform.runLater(() -> ((Stage) editor.getScene().getWindow()).titleProperty().bind(
                Bindings.when(editorModel.filePathProperty().isNotNull())
                        .then(editorModel.filePathProperty())
                        .otherwise("Unnamed")
                        .concat(Bindings.when(editorModel.modifiedProperty())
                                .then("*")
                                .otherwise("")))
        );
    }

    @FXML
    private void onAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("About");
        about.setHeaderText("JavaFX Text Editor");
        about.setContentText("""
            Java version: %s, %s
            JavaFX version: %s
            """.formatted(System.getProperty("java.version"), System.getProperty("java.vendor"), System.getProperty("javafx.version")));
        about.showAndWait();
    }

    @FXML
    private void onNew(ActionEvent event) {
       editorModel.reset();
    }

    @FXML
    private void onOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Logger.debug("Opening file {}", file);
            try {
                editorModel.open(file.getPath());
            } catch (IOException e) {
                Logger.error("Failed to open file");
            }
        }
    }

    @FXML
    private void onSave(ActionEvent event) {
        if (editorModel.getFilePath() != null) {
            Logger.debug("Saving file");
            try {
                editorModel.save();
            } catch (IOException e) {
                Logger.error(e, "Failed to save file");
            }
        } else {
            performSaveAs();
        }
    }

    @FXML
    private void onSaveAs(ActionEvent event) {
        performSaveAs();
    }

    private void performSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            Logger.debug("Saving file as {}", file);
            try {
                editorModel.saveAs(file.getPath());
            } catch (IOException e) {
                Logger.error(e, "Failed to save file");
            }
        }
    }

    @FXML
    private void onQuit(ActionEvent event) {
        Logger.info("Terminating");
        Platform.exit();
    }

}
