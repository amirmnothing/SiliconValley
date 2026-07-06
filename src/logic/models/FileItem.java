package logic.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileItem {
    private final StringProperty name;
    private final StringProperty date;
    private final File file;

    public FileItem(File file) {
        this.file = file;
        this.name = new SimpleStringProperty(file.getName());
        this.date = new SimpleStringProperty(getFileCreationOrModifiedDate(file));
    }

    private String getFileCreationOrModifiedDate(File file) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            long timeInMillis = attrs.lastModifiedTime().toMillis();
            Date date = new Date(timeInMillis);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return formatter.format(date);

        } catch (Exception e) {
            return "Unknown Date";
        }
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public File getFile() {
        return file;
    }
}