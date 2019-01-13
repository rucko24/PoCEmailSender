package com.example.javamail.util;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.vaadin.easyuploads.MultiFileUpload;
import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

@SpringComponent
@UIScope
public class UploadComponent extends MultiFileUpload implements ShowData {

    private String fileName;
    private String mimeType;
    private transient Path path;

    @PostConstruct
    public void init() {
        initComponents();
    }

    private void initComponents() {
        setWidth("100%");
        setAreaText("Adjuntar");
    }

    @Override
    protected void handleFile(File file, String fileName, String mimeType, long l) {
        //FileTypeResolver
        this.fileName = fileName;
        this.mimeType = mimeType;
        try {
            path = Files.write(Paths.get("src/main/resources/" + fileName),
                    Files.readAllBytes(file.toPath()) ,
                    StandardOpenOption.CREATE);

        } catch (IOException e) {
            getLogger().error(null,e);
        }
        this.mimeType = mimeType;

    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UploadComponent)) return false;
        if (!super.equals(o)) return false;
        UploadComponent that = (UploadComponent) o;
        return Objects.equals(fileName, that.fileName) &&
                Objects.equals(mimeType, that.mimeType) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fileName, mimeType, path);
    }
}
