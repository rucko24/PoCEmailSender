package com.example.javamail;

import com.example.javamail.layouts.FormEmailSender;
import com.github.appreciated.app.layout.AppLayout;
import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.design.AppLayoutDesign;
import com.github.appreciated.app.layout.builder.entities.DefaultBadgeHolder;
import com.github.appreciated.app.layout.builder.entities.DefaultNotificationHolder;
import com.github.appreciated.app.layout.builder.factories.DefaultSpringNavigationElementInfoProducer;
import com.github.appreciated.app.layout.component.MenuHeader;
import com.github.appreciated.app.layout.component.button.AppBarNotificationButton;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;


import static com.github.appreciated.app.layout.builder.Section.HEADER;

/**
 * @author rub´n
 **/
@SpringUI
@Theme("mytheme")
@Push
@PushStateNavigation // url paths con push
@Viewport("initial-scale=1, maximum-scale=1")
public class ExampleUI extends UI {

    private DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private DefaultBadgeHolder badge = new DefaultBadgeHolder();

    @Autowired
    private SpringViewProvider springViewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Poc-EmailSender");

        setContent(AppLayout.getCDIBuilder(Behaviour.LEFT_RESPONSIVE_HYBRID)
                .withViewProvider(() -> springViewProvider)
                .withNavigationElementInfoProducer(new DefaultSpringNavigationElementInfoProducer())
                .withTitle("PoC Email Sender")
                .addToAppBar(new AppBarNotificationButton(notifications, true))
                .withDesign(AppLayoutDesign.MATERIAL)
                .add(new MenuHeader("v1.0 by rubn", new ThemeResource("img/rubn.jpg")), HEADER)
                .add("Home", VaadinIcons.ARROW_RIGHT, badge, FormEmailSender.class)
                .build());
    }


}
