package editor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EditorModel {

    private final StringProperty filePath = new SimpleStringProperty(null);

    private final StringProperty content = new SimpleStringProperty("");

    private final BooleanProperty modified = new SimpleBooleanProperty(false);

    public EditorModel() {
        content.addListener((observable, oldValue, newValue) -> {
            modified.set(true);
            Logger.debug("Content changed");
        });
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    public final String getFilePath() {
        return filePath.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public final String getContent() {
        return content.get();
    }

    public BooleanProperty modifiedProperty() {
        return modified;
    }

    public final boolean getModified() {
        return modified.get();
    }

    public void reset() {
        filePath.set(null);
        content.set("");
        modified.set(false);
    }

    public void open(String filePath) throws IOException {
        var s = Files.readString(Path.of(filePath));
        this.filePath.set(filePath);
        content.set(s);
        modified.set(false);
    }

    public void save() throws IOException {
        if (filePath.get() == null) {
            throw new IllegalStateException();
        }
        saveAs(filePath.get());
    }

    public void saveAs(String filePath) throws IOException {
        Files.writeString(Path.of(filePath), content.get());
        this.filePath.set(filePath);
        modified.set(false);
    }

}
