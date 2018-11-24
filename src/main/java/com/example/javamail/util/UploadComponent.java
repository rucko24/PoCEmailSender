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

@SpringComponent
@UIScope
public class UploadComponent extends MultiFileUpload {

    private String fileName;
    private String mimeType;
    private Path path;

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
            e.printStackTrace();
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
}
