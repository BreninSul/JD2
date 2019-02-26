package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.managers.ToSimpleDate;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertificateBox extends Div {
    public static final String LABLE_WIDTH = "10vw";
    public static final String INFO_WIDTH = "15vw";
    public static final String BOX_HEIGHT = "30vh";
    public static final String BOX_WIDTH = "28vw";
    public static final String FONT_SIZE = "12px";


    public CertificateBox(Certificate certificate) {
        String route = UI.getCurrent().getRouter()
                .getUrl(CabinetView.class);
        getStyle().set("border","4px double black");
        HorizontalLayout name = new HorizontalLayout();
        Label nameLable = new Label(getTranslation("search.name"));
        nameLable.setWidth(LABLE_WIDTH);
        Label nameInfo = new Label(certificate.getProductName());
        nameInfo.getStyle().set("font-size", FONT_SIZE);
        nameInfo.setWidth(INFO_WIDTH);
        name.add(nameLable, nameInfo);
        HorizontalLayout number = new HorizontalLayout();
        Label numberLable = new Label(getTranslation("search.number"));
        Label numberInfo = new Label(certificate.getNumber());
        numberLable.setWidth(LABLE_WIDTH);
        numberInfo.setWidth(INFO_WIDTH);
        numberInfo.getStyle().set("font-size", FONT_SIZE);
        number.add(numberLable, numberInfo);
        HorizontalLayout applicant = new HorizontalLayout();
        Label applicantLable = new Label(getTranslation("search.applicant"));
        Label applicantInfo = new Label(certificate.getApplicant());
        applicantLable.setWidth(LABLE_WIDTH);
        applicantInfo.setWidth(INFO_WIDTH);
        applicantInfo.getStyle().set("font-size", FONT_SIZE);
        applicant.add(applicantLable, applicantInfo);
        HorizontalLayout manufacturer = new HorizontalLayout();
        Label manufacturerLable = new Label(getTranslation("search.manufacturer"));
        Label manufacturerInfo = new Label(certificate.getManufacturer());
        manufacturerLable.setWidth(LABLE_WIDTH);
        manufacturerInfo.setWidth(INFO_WIDTH);
        manufacturerInfo.getStyle().set("font-size", FONT_SIZE);
        manufacturer.add(manufacturerInfo, manufacturerLable);
        HorizontalLayout bdate = new HorizontalLayout();
        Label bdateLable = new Label(getTranslation("search.begindate"));
        Label bdateInfo = new Label(ToSimpleDate.get(certificate.getBeginDate()));
        bdateLable.setWidth(LABLE_WIDTH);
        bdateInfo.setWidth(INFO_WIDTH);
        bdateInfo.getStyle().set("font-size", FONT_SIZE);
        bdate.add(bdateLable, bdateInfo);
        HorizontalLayout edate = new HorizontalLayout();
        Label edateLable = new Label(getTranslation("search.enddate"));
        Label edateInfo = new Label(ToSimpleDate.get(certificate.getEndDate()));
        edateInfo.getStyle().set("font-size", FONT_SIZE);
        edateLable.setWidth(LABLE_WIDTH);
        edateInfo.setWidth(INFO_WIDTH);
        edate.add(edateLable, edateInfo);

        Button save = new Button();
        save.setText(getTranslation("search.button.save"));
        add(name, number, bdate, edate);
        ContextMenu contextMenu=new ContextMenu();
        contextMenu.add(save);
        getStyle().set("overflow-y","auto");
        getStyle().set("overflow-x","visible");
        getStyle().set("min-width",BOX_WIDTH);
        setHeight(BOX_HEIGHT);
        setWidth(BOX_WIDTH);
        if (ViewUtils.isLoggined()) {
            contextMenu.setTarget(this);
        }
        save.addClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("WantedCert", certificate);
            UI.getCurrent().navigate(CabinetView.class);
        });
    }
}

