package com.example.javamail;

import com.example.javamail.util.ShowData;
import com.example.javamail.util.UploadComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author rub´n
 **/
@Service
public class EmailSender implements ShowData {

    private static final String MAIL_PROPERTIES = "src/main/resources/mail.properties";
    private String destinatario;
    private String descripcion;
    private UI ui;
    private ProgressBar bar;
    private UploadComponent uploadComponent;

    @Value("${mail.smtp.username}")
    private String userName;

    @Value("${mail.smtp.password}")
    private String password;

    /**
     * Envio de correo sin adjunto
     *
     */
    public void send() throws MessagingException, IOException {
        send(Arrays.asList(destinatario), descripcion, null, null, null);
    }


    /**
     * Envio de mail con un adjunto.
     *
     * @param titulo   titulo del mensaje a enviar.
     * @param adjunto  archivo adjunto a incluir.
     * @param nombre   el nombre del archivo
     * @param mimeType mimetype del adjunto.
     * @throws MessagingException
     * @throws IOException
     */
    public void send(String titulo, InputStream adjunto, String nombre, String mimeType) throws MessagingException, IOException {
        send(Arrays.asList(destinatario), titulo, Arrays.asList(adjunto), Arrays.asList(nombre), Arrays.asList(mimeType));
    }

    /**
     * Envio de emails con ajuntos
     *
     * @param destinatario el destinatario del email
     * @param titulo       titulo del mensaje a enviar.
     * @param adjuntos     archivo adjunto a incluir.
     * @param nombre       el nombre del archivo
     * @param mimeType     mimetype del adjunto.
     * @throws MessagingException
     * @throws IOException
     */
    public void send(Collection<String> destinatario, String titulo,
                     List<InputStream> adjuntos, List<String> nombre, List<String> mimeType)
            throws MessagingException, IOException {

        final Properties prpts = new Properties();
        final Path pathMailProperties = Paths.get(MAIL_PROPERTIES);
        try (final BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(pathMailProperties))) {
            prpts.load(bis);
        }
        // conectando con el servidor SMTP
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(prpts);
        mailSender.setUsername(userName);
        mailSender.setPassword(password);

        final MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("sendTest@gmail.com"); // por defecto
        helper.setSubject(titulo);
        helper.setSentDate(new Date());
        helper.setText(this.descripcion, true);

        destinatario.forEach(index -> {
            try {
                helper.setTo(InternetAddress.parse(replace(index)));
            } catch (MessagingException e) {
                getLogger().error(null,e);
                ui.access(() -> {
                    Notification.show("Error con destinatarios", Notification.Type.ERROR_MESSAGE);
                    bar.setVisible(false);
                });
            }
        });

        if (Objects.nonNull(adjuntos)) {

            IntStream.range(0, adjuntos.size())
                    .parallel()
                    .forEachOrdered(count -> {
                        try {
                            final ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(adjuntos.get(count), mimeType.get(count));
                            helper.addAttachment(nombre.get(count), byteArrayDataSource);
                        } catch (Exception ex) {
                            getLogger().error(null,ex);
                        }
                    });
        }

        mailSender.send(message);
        getLogger().info("Email enviado");
        ui.access(() -> {
            Notification.show("Email enviado");
            bar.setVisible(false);
        }); // invocar al metodo access para modificar la UI desde un background thread
    }


    /**
     * @param ui           intancia de la UI
     * @param destinatario destinatario
     * @param descripcion  descripcion del correo a enviar
     * @param upc          objeto con configuracion del archivo subido
     */
    public void setData(final UI ui, String destinatario, String descripcion, ProgressBar bar,
                        final UploadComponent upc) {
        this.destinatario = destinatario;
        this.ui = ui;
        this.descripcion = descripcion;
        this.bar = bar;
        this.uploadComponent = upc;
    }

    public UploadComponent getUploadComponent() {
        return uploadComponent;
    }

    private static String replace(final String r) {
        //caracteres permitidos ;-,espacio
        final String COMA = ", ";
        final String replaceString = r.trim();
        return replaceString.replace(" ", COMA)
                .replace(";", COMA)
                .replace("; ", COMA)
                .replace("- ", COMA)
                .replace("-", COMA)
                .replace(",", COMA);

    }

}
