package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.dao.registry.InfoEntity;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.service.RegestryService;
import com.gmail.breninsul.jd2.service.impl.RegestryServiceImpl;
import com.gmail.breninsul.jd2.view.menu.Menu;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Route(value = "search", layout = Menu.class)
public class SearchView extends VerticalLayout {

    public static final String SEARCH_DIV_WIDTH = "30vw";
    public static final String SEARCH_FIELS_WIDTH = "30vw";
    public static final String WIDTH = "90vw";
    private H3 h3rfD = new  H3();
    private H3 h3rfC = new  H3();
    private H3 h3byD = new  H3();
    private H3 h3byC = new  H3();
    private H3 h3armD = new H3();
    private H3 h3armC = new H3();
    private H3 h3kzD = new  H3();
    private H3 h3kzC = new  H3();
    private H3 h3kgD = new  H3();
    private H3 h3kgC = new  H3();

    public SearchView(RegestryService regestryService) {
        HorizontalLayout search = new HorizontalLayout();
        String route = UI.getCurrent().getRouter()
                .getUrl(getClass());
        search.setWidth(WIDTH);
        TextField searchField = new TextField();
        Div div = new Div();
        div.setWidth(SEARCH_DIV_WIDTH);
        Div div2 = new Div();
        div2.setWidth(SEARCH_DIV_WIDTH);
        searchField.setLabel(getTranslation("search.field"));
        Button searchButton = new Button();
        Icon icon = VaadinIcon.SEARCH.create();
        searchButton.setIcon(icon);
        //searchButton.getStyle().set("background", "green");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        search.add(div, searchField, searchButton, div2);
        searchField.setWidth(SEARCH_FIELS_WIDTH);
        search.setVerticalComponentAlignment(Alignment.END, searchButton);
        add(search);
        HorizontalLayout byDLayout = new HorizontalLayout();
        HorizontalLayout byCLayout = new HorizontalLayout();
        HorizontalLayout armDLayout = new HorizontalLayout();
        HorizontalLayout armCLayout = new HorizontalLayout();
        HorizontalLayout kzDLayout = new HorizontalLayout();
        HorizontalLayout kzCLayout = new HorizontalLayout();
        HorizontalLayout kgDLayout = new HorizontalLayout();
        HorizontalLayout kgCLayout = new HorizontalLayout();
        HorizontalLayout rfDLayout = new HorizontalLayout();
        HorizontalLayout rfCLayout = new HorizontalLayout();
        Details rfDdetails = new Details();
        Details rfCdetails = new Details();
        Details byDdetails = new Details();
        Details byCdetails = new Details();
        Details armDdetails = new Details();
        Details armCdetails = new Details();
        Details kzDdetails = new Details();
        Details kzCdetails = new Details();
        Details kgDdetails = new Details();
        Details kgCdetails = new Details();
        HorizontalLayout ruDl = new HorizontalLayout(new EmptyDiv(), rfDdetails);
        HorizontalLayout ruCl = new HorizontalLayout(new EmptyDiv(), rfCdetails);
        HorizontalLayout byDl = new HorizontalLayout(new EmptyDiv(), byDdetails);
        HorizontalLayout byCl = new HorizontalLayout(new EmptyDiv(), byCdetails);
        HorizontalLayout armDl = new HorizontalLayout(new EmptyDiv(), armDdetails);
        HorizontalLayout armCl = new HorizontalLayout(new EmptyDiv(), armCdetails);
        HorizontalLayout kzDl = new HorizontalLayout(new EmptyDiv(), kzDdetails);
        HorizontalLayout kzCl = new HorizontalLayout(new EmptyDiv(), kzCdetails);
        HorizontalLayout kgDl = new HorizontalLayout(new EmptyDiv(), kgDdetails);
        HorizontalLayout kgCl = new HorizontalLayout(new EmptyDiv(), kgCdetails);
        rfDdetails.addContent(new EmptyDiv());
        rfCdetails.addContent(new EmptyDiv());
        byDdetails.addContent(new EmptyDiv());
        byCdetails.addContent(new EmptyDiv());
        armDdetails.addContent(new EmptyDiv());
        armCdetails.addContent(new EmptyDiv());
        kzDdetails.addContent(new EmptyDiv());
        kzCdetails.addContent(new EmptyDiv());
        kgDdetails.addContent(new EmptyDiv());
        kgCdetails.addContent(new EmptyDiv());
        rfDdetails.setEnabled(false);
        rfCdetails.setEnabled(false);
        byDdetails.setEnabled(false);
        byCdetails.setEnabled(false);
        armDdetails.setEnabled(false);
        armCdetails.setEnabled(false);
        kzDdetails.setEnabled(false);
        kzCdetails.setEnabled(false);
        kgDdetails.setEnabled(false);
        kgCdetails.setEnabled(false);
        rfDdetails.setSummary(new HorizontalLayout(new H3(getTranslation("search.rf.decl")), h3rfD));
        rfCdetails.setSummary(new HorizontalLayout(new H3(getTranslation("search.rf.cert")), h3rfC));
        byDdetails.setSummary(new HorizontalLayout(new H3(getTranslation("search.by.decl")), h3byD));
        byCdetails.setSummary(new HorizontalLayout(new H3(getTranslation("search.by.cert")), h3byC));
        armDdetails.setSummary(new HorizontalLayout(new H3(getTranslation("search.arm.decl")), h3armD));
        armCdetails.setSummary(new HorizontalLayout(new H3(getTranslation("search.arm.cert")), h3armC));
        kzDdetails.setSummary(new HorizontalLayout(new H3(getTranslation("search.kz.decl")), h3kzD));
        kzCdetails.setSummary(new HorizontalLayout(new H3(getTranslation("search.kz.cert")), h3kzC));
        kgDdetails.setSummary(new HorizontalLayout(new H3(getTranslation("search.kg.decl")), h3kgD));
        kgCdetails.setSummary(new HorizontalLayout(new H3(getTranslation("search.kg.cert")), h3kgC));
        rfDdetails.addContent(rfDLayout);
        rfDdetails.setOpened(true);
        rfCdetails.setOpened(true);
        rfCdetails.addContent(rfCLayout);
        byDdetails.addContent(byDLayout);
        byCdetails.addContent(byCLayout);
        byDdetails.setOpened(true);
        byCdetails.setOpened(true);
        armDdetails.addContent(armDLayout);
        armCdetails.addContent(armCLayout);
        armDdetails.setOpened(true);
        armCdetails.setOpened(true);
        kzDdetails.addContent(kzDLayout);
        kzCdetails.addContent(kzCLayout);
        kzDdetails.setOpened(true);
        kzCdetails.setOpened(true);
        kgDdetails.addContent(kgDLayout);
        kgCdetails.addContent(kgCLayout);
        kgDdetails.setOpened(true);
        kgCdetails.setOpened(true);
/*        Div rfD = new Div(rfDdetails);
        Div rfC = new Div(rfCdetails);
        Div byD = new Div(byDdetails);
        Div byC = new Div(byCdetails);
        Div armD = new Div(armDdetails);
        Div armC = new Div(armCdetails);
        Div kzD = new Div(kzDdetails);
        Div kzC = new Div(kzCdetails);
        Div kgD = new Div(kgDdetails);
        Div kgC = new Div(kgCdetails);*/
        Div rfD = new Div(ruDl);
        Div rfC = new Div(ruCl);
        Div byD = new Div(byDl);
        Div byC = new Div(byCl);
        Div armD = new Div(armDl);
        Div armC = new Div(armCl);
        Div kzD = new Div(kzDl);
        Div kzC = new Div(kzCl);
        Div kgD = new Div(kgDl);
        Div kgC = new Div(kgCl);
        addDiv(rfD);
        addDiv(rfC);
        addDiv(byD);
        addDiv(byC);
        addDiv(armD);
        addDiv(armC);
        addDiv(kzD);
        addDiv(kzC);
        addDiv(kgD);
        addDiv(kgC);
        setFlexGrow(1);
        String value = "";
        try {
            value = VaadinService.getCurrentRequest().getParameterMap().get("search")[0];
        } catch (NullPointerException e) {
        }
        if (!value.isEmpty()) {
            initSearchResults(regestryService, value, byDLayout, byCLayout, armDLayout, armCLayout, kzDLayout, kzCLayout, kgDLayout, kgCLayout, rfDLayout, rfCLayout, rfDdetails, rfCdetails, byDdetails, byCdetails, armDdetails, armCdetails, kzDdetails, kzCdetails, kgDdetails, kgCdetails);
        }
        searchField.setPlaceholder(value);
        searchButton.addClickListener(e -> {
            String searchParameter = searchField.getValue();
            List<String> searchParametres = new ArrayList<String>();
            searchParametres.add(searchParameter);
            Map<String, List<String>> paramsMap = new HashMap<>();
            paramsMap.put("search", searchParametres);
            QueryParameters queryParameters = new QueryParameters(paramsMap);
            UI.getCurrent().navigate(route, queryParameters);
            initSearchResults(regestryService, searchParameter, byDLayout, byCLayout, armDLayout, armCLayout, kzDLayout, kzCLayout, kgDLayout, kgCLayout, rfDLayout, rfCLayout, rfDdetails, rfCdetails, byDdetails, byCdetails, armDdetails, armCdetails, kzDdetails, kzCdetails, kgDdetails, kgCdetails);
        });
    }

    private void initSearchResults(RegestryService regestryService, String searchField, HorizontalLayout byDLayout, HorizontalLayout byCLayout, HorizontalLayout armDLayout, HorizontalLayout armCLayout, HorizontalLayout kzDLayout, HorizontalLayout kzCLayout, HorizontalLayout kgDLayout, HorizontalLayout kgCLayout, HorizontalLayout rfDLayout, HorizontalLayout rfCLayout, Details rfDdetails, Details rfCdetails, Details byDdetails, Details byCdetails, Details armDdetails, Details armCdetails, Details kzDdetails, Details kzCdetails, Details kgDdetails, Details kgCdetails) {
        InfoEntity entities = regestryService.get(searchField, 100, 20);
        List<Certificate> rfDlist = entities.get(InfoEntity.RF_DECL);
        List<Certificate> rfClist = entities.get(InfoEntity.RF_CERT);
        List<Certificate> byDlist = entities.get(InfoEntity.BELARUS_DECL);
        List<Certificate> byClist = entities.get(InfoEntity.BELARUS_CERT);
        List<Certificate> armDlist = entities.get(InfoEntity.ARM_DECL);
        List<Certificate> armClist = entities.get(InfoEntity.ARM_CERT);
        List<Certificate> kzDlist = entities.get(InfoEntity.KZ_DECL);
        List<Certificate> kzClist = entities.get(InfoEntity.KZ_CERT);
        List<Certificate> kgDlist = entities.get(InfoEntity.KGZ_DECL);
        List<Certificate> kgClist = entities.get(InfoEntity.KGZ_CERT);
        rfCLayout.removeAll();
        rfDLayout.removeAll();
        byCLayout.removeAll();
        byDLayout.removeAll();
        armCLayout.removeAll();
        armDLayout.removeAll();
        kzCLayout.removeAll();
        kzDLayout.removeAll();
        kgCLayout.removeAll();
        kgDLayout.removeAll();
        try {
            h3rfD.setText(" (" + rfDlist.size() + ")");
        } catch (NullPointerException e) {
            h3rfD.setText(" (0)");
        }
        if (rfDlist == null) {
            rfDLayout.add(new H3(getTranslation("search.server.error")));
        } else {
            if (rfDlist.isEmpty()) {
                rfDLayout.add(new H3(getTranslation("search.server.noresult")));
            } else {
                for (Certificate c : rfDlist) {
                    rfDLayout.add(new CertificateBox(c));
                }
            }
        }
        try {
            h3rfC.setText(" (" + rfClist.size() + ")");
        } catch (NullPointerException e) {
            h3rfC.setText(" (0)");
        }
        if (rfClist == null) {
            rfCLayout.add(new H3(getTranslation("search.server.error")));
        } else {
            if (rfClist.isEmpty()) {
                rfCLayout.add(new H3(getTranslation("search.server.noresult")));
            } else {
                for (Certificate c : rfClist) {
                    rfCLayout.add(new CertificateBox(c));
                }
            }
        }
        try {
            h3byD.setText(" (" + byDlist.size() + ")");
        } catch (NullPointerException e) {
            h3byD.setText(" (0)");
        }
        if (byDlist == null) {
            byDLayout.add(new H3(getTranslation("search.server.error")));
        } else {
            if (byDlist.isEmpty()) {
                byDLayout.add(new H3(getTranslation("search.server.noresult")));
            } else {
                for (Certificate c : byDlist) {
                    byDLayout.add(new CertificateBox(c));
                }
            }
        }
        try {
            h3byC.setText(" (" + byClist.size() + ")");
        } catch (NullPointerException e) {
            h3byC.setText(" (0)");
        }
        if (byClist == null) {
            byCLayout.add(new H3(getTranslation("search.server.error")));
        } else {
            if (byClist.isEmpty()) {
                byCLayout.add(new H3(getTranslation("search.server.noresult")));
            } else {
                for (Certificate c : byClist) {
                    byCLayout.add(new CertificateBox(c));
                }
            }
        }
        try {
            h3armD.setText(" (" + armDlist.size() + ")");
        } catch (NullPointerException e) {
            h3armD.setText(" (0)");
        }
        if (armDlist == null) {
            armDLayout.add(new H3(getTranslation("search.server.error")));
        } else {
            if (armDlist.isEmpty()) {
                armDLayout.add(new H3(getTranslation("search.server.noresult")));
            } else {
                for (Certificate c : armDlist) {
                    armDLayout.add(new CertificateBox(c));
                }
            }
        }
        try {
        h3armC.setText(" (" + armClist.size() + ")");
        } catch (NullPointerException e) {
            h3armC.setText(" (0)");
        }
        if (armClist == null) {
            armCLayout.add(new H3(getTranslation("search.server.error")));
        } else {
            if (armClist.isEmpty()) {
                armCLayout.add(new H3(getTranslation("search.server.noresult")));
            } else {
                for (Certificate c : armClist) {
                    armCLayout.add(new CertificateBox(c));
                }
            }
        }
        try{
        h3kzD.setText(" (" + kzDlist.size() + ")");
        } catch (NullPointerException e) {
            h3kzD.setText(" (0)");
        }
        if (kzDlist == null) {
            kzDLayout.add(new H3(getTranslation("search.server.error")));
        } else {
            if (kzDlist.isEmpty()) {
                kzDLayout.add(new H3(getTranslation("search.server.noresult")));
            } else {
                for (Certificate c : kzDlist) {
                    kzDLayout.add(new CertificateBox(c));
                }
            }
        }
        try {
            h3kzC.setText(" (" + kzClist.size() + ")");
        } catch (NullPointerException e) {
            h3kzC.setText(" (0)");
        }
        if (kzClist == null) {
            kzCLayout.add(new H3(getTranslation("search.server.error")));
        } else {
            if (kzClist.isEmpty()) {
                kzCLayout.add(new H3(getTranslation("search.server.noresult")));
            } else {
                for (Certificate c : kzClist) {
                    kzCLayout.add(new CertificateBox(c));
                }
            }
        }
        try{
        h3kgD.setText(" (" + kgDlist.size() + ")");
        } catch (NullPointerException e) {
            h3kgD.setText(" (0)");
        }
        if (kgDlist == null) {
            kgDLayout.add(new H3(getTranslation("search.server.error")));
        } else {
            if (kgDlist.isEmpty()) {
                kgDLayout.add(new H3(getTranslation("search.server.noresult")));
            } else {
                for (Certificate c : kgDlist) {
                    kgDLayout.add(new CertificateBox(c));
                }
            }
        }
        try{
        h3kgC.setText(" (" + kgClist.size() + ")");
        } catch (NullPointerException e) {
            h3kgC.setText(" (0)");
        }
        if (kgClist == null) {
            kgCLayout.add(new H3(getTranslation("search.server.error")));
        } else {
            if (kgClist.isEmpty()) {
                kgCLayout.add(new H3(getTranslation("search.server.noresult")));
            } else {
                for (Certificate c : kgClist) {
                    kgCLayout.add(new CertificateBox(c));
                }
            }
        }
        rfDdetails.setEnabled(true);
        rfCdetails.setEnabled(true);
        byDdetails.setEnabled(true);
        byCdetails.setEnabled(true);
        armDdetails.setEnabled(true);
        armCdetails.setEnabled(true);
        kzDdetails.setEnabled(true);
        kzCdetails.setEnabled(true);
        kgDdetails.setEnabled(true);
        kgCdetails.setEnabled(true);
    }

    private void addDiv(Div rfD) {
        add(rfD);
        rfD.getStyle().set("overflow", "auto");
        rfD.setWidth("98vw");
    }


}
