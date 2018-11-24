package com.example.javamail.executarNextMonth;

import com.example.javamail.EmailSender;
import com.example.javamail.util.ShowData;
import com.example.javamail.util.UploadComponent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@SpringComponent
public class TimerClass implements Job, ShowData {

    @Autowired
    private EmailSender emailSender;

    private void sendMail() {
        final UploadComponent uploadComponent = emailSender.getUploadComponent();
        final String nombreArchivo = uploadComponent.getFileName();
        final String titulo = "Test send file";
        final String mimeType = uploadComponent.getMimeType();
        final Path file = uploadComponent.getPath();

        try (final BufferedInputStream attachment = new BufferedInputStream(Files.newInputStream(file))) {

            emailSender.send(titulo, attachment, nombreArchivo, mimeType);

        } catch (IOException | MessagingException ex) {
            ex.printStackTrace();
            UI.getCurrent().access(() -> Notification.show("Error al enviar"));
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        sendMail();
    }

}
