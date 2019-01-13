package com.example.javamail;

import com.vaadin.ui.UI;

@FunctionalInterface
public interface SetDataForMail {

    void setDataForMail(UI ui, String destinario, String descripcion);

}
