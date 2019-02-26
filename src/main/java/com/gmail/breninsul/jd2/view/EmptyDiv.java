package com.gmail.breninsul.jd2.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;

public class EmptyDiv extends Div {

    public static final String VW = "100px";

    public EmptyDiv() {
        getStyle().set("min-width", VW);
        Element element= ElementFactory.createDiv();
        element.getStyle().set("min-width", VW);
        getElement().appendChild(element);
        getStyle().set("overflow","visible");
    }
}
