package com.example.javamail.layouts;

import com.example.javamail.SetDataForMail;
import com.example.javamail.EmailSender;
import com.example.javamail.executarNextMonth.RunNextTask;
import com.example.javamail.util.ShowData;
import com.example.javamail.util.UploadComponent;
import com.example.javamail.util.ValidateMultipleMails;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.Stream;

@UIScope
@SpringView(name = "") // in Spring an empty view name is also the default view
@MenuCaption("Home View")
@MenuIcon(VaadinIcons.HOME)
public class FormEmailSender extends HorizontalLayout implements ShowData, View, SetDataForMail {

    private final Label labelTitulo = new Label("PoC Email Sender");
    private final ProgressBar progressBar = new ProgressBar();
    private final Binder<FormEmailBean> binder = new Binder<>(FormEmailBean.class);
    private final TextArea descripcion = new TextArea("Descripción");
    private final ComboBox<String> tiempos = new ComboBox<String>("Tiempo en");
    private final Slider slider = new Slider("Retardo");
    private final TextField para = new TextField("Para");
    private final Button buttonValidate = new Button("Enviar email");
    private final FormLayout formLayout = new FormLayout();
    private String tiposDeTiempos;
    private final Label labelExpresionCron = new Label();
    private final VerticalLayout verticalLayoutContent = new VerticalLayout();

    private UploadComponent uploadComponent;

    @Autowired
    private RunNextTask runNextTask;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    public FormEmailSender(UploadComponent uploadComponent) {
        this.uploadComponent = uploadComponent;
        initLayout();
    }

    private void initLayout() {
        //header
        final HorizontalLayout header = new HorizontalLayout(labelTitulo, progressBar);
        progressBar.setVisible(false);
        progressBar.setIndeterminate(true);
        labelTitulo.addStyleNames("no-margin","h1","bold","colored");
        header.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
        header.setExpandRatio(progressBar, 1);

        //components of form
        para.setDescription("separar correos con ,");
        para.setPlaceholder("test@test.com");
        descripcion.setPlaceholder("descripción del mensaje...");
        Stream.of(tiempos, descripcion, buttonValidate, slider, para).forEach(with -> with.setWidth("100%"));


        //row button
        final HorizontalLayout row = new HorizontalLayout(buttonValidate);
        row.setWidth("100%");

        //label with result
        labelExpresionCron.setContentMode(ContentMode.HTML);
        labelExpresionCron.addStyleNames("bold", "h2","no-margin");
        labelExpresionCron.setWidth("30%");
        labelExpresionCron.addContextClickListener(e -> verticalLayoutContent.removeComponent(labelExpresionCron));

        //Icons
        para.setIcon(VaadinIcons.INBOX);
        buttonValidate.setIcon(VaadinIcons.ARROW_CIRCLE_UP);
        slider.setIcon(VaadinIcons.TIME_FORWARD);
        tiempos.setIcon(VaadinIcons.TIMER);
        descripcion.setIcon(VaadinIcons.FILE_TEXT);

        //formlayout
        formLayout.addComponents(para, descripcion, tiempos, slider, uploadComponent, row);
        formLayout.setWidth("30%");

        //layout with content
        verticalLayoutContent.addComponents(header,formLayout, labelExpresionCron);
        verticalLayoutContent.setExpandRatio(labelExpresionCron,1);

        final Panel panel = new Panel(verticalLayoutContent);
        panel.setSizeFull();
        addComponent(panel);
        setMargin(true);
        setSizeFull();

        initBinder();
        initBehaviour();
    }

    private void initBinder() {
        binder.forField(descripcion)
                .asRequired()
                .withValidator(new StringLengthValidator("Mas de 10 caracteres", 10, null))
                .bind("description");
        binder.forField(para)
                .asRequired("al menos un correo")
                .withValidator(new ValidateMultipleMails("email invalido"))
                .bind(FormEmailBean::getForUser, FormEmailBean::setForUser);
    }

    /**
     * Este es el listener del boton que simula un evento para enviar un correo a un tiempo determinado
     */
    private void initBehaviour() {

        tiempos.setItems(Arrays.asList("Segundos", "Minutos", "Horas", "Meses"));
        tiempos.setEmptySelectionAllowed(false);
        tiempos.addValueChangeListener(e -> {
            tiposDeTiempos = e.getValue().toUpperCase();
        });

        buttonValidate.addClickListener(e -> {
            final BinderValidationStatus<FormEmailBean> status = binder.validate();
            if (!status.hasErrors()) {
                setDataForMail(getUI(), para.getValue().trim(), descripcion.getValue().trim());
                final Integer valueTime = slider.getValue().intValue();
                addLabelResult();
                runNextTask.initTimeBuilder(tiposDeTiempos, valueTime, labelExpresionCron);

            } else {
                Notification.show("Invalid form", Notification.Type.ERROR_MESSAGE);
            }
        });

    }

    private void addLabelResult() {
        progressBar.setVisible(true);
        labelExpresionCron.addStyleName("label-result");
        verticalLayoutContent.addComponents(labelExpresionCron);
    }

    @Override
    public void setDataForMail(UI ui, String destinario, String descripcion) {
        emailSender.setData(ui, destinario, descripcion, progressBar,uploadComponent);
    }

}
