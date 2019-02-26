package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.config.ProductTypes;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import com.gmail.breninsul.jd2.service.CertificateService;
import com.gmail.breninsul.jd2.service.ProductService;
import com.gmail.breninsul.jd2.service.UserService;
import com.google.common.base.Splitter;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.server.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.gmail.breninsul.jd2.view.LoginView.TIMEOUT;

@Log4j2
public class ProductBox extends Div {
    public static final String LABLE_WIDTH = "10vw";
    public static final String INFO_WIDTH = "15vw";
    public static final String BOX_HEIGHT = "50vh";
    public static final String FIELD_HEIGHT = "20vh";
    public static final String BOX_WIDTH = "30vw";
    public static final String FONT_SIZE = "14px";
    public static final String IMAGE_SIZE = "200px";
    public static final String DIALOG_WIDTH = "80vw";
    public static final String DIALOG_HEIGHT = "60vh";
    public static final String FIELD_WIDTH = "20vw";

    public ProductBox(CertificateService certificateService, ProductService productService, Product product) {
        getStyle().set("border", "4px double black");
        boolean haveToAddCert = true;
        Certificate wantedCertificate = (Certificate) VaadinSession.getCurrent().getAttribute("WantedCert");
        if (wantedCertificate == null) {
            log.info("no cert to add");
            haveToAddCert = false;
        }
        log.info("cert to add=" + wantedCertificate);
        HorizontalLayout name = new HorizontalLayout();
        Label nameLable = new Label(getTranslation("product.name"));
        nameLable.setWidth(LABLE_WIDTH);
        Label nameInfo = new Label(product.getProductName());
        nameInfo.getStyle().set("font-size", FONT_SIZE);
        nameInfo.setWidth(INFO_WIDTH);
        name.add(nameLable, nameInfo);
        HorizontalLayout manufacturer = new HorizontalLayout();
        Label manufacturerLable = new Label(getTranslation("product.manufacturer"));
        Label manufacturerInfo = new Label(product.getManufacturer());
        manufacturerLable.setWidth(LABLE_WIDTH);
        manufacturerInfo.setWidth(INFO_WIDTH);
        manufacturerInfo.getStyle().set("font-size", FONT_SIZE);
        manufacturer.add(manufacturerLable, manufacturerInfo);
        HorizontalLayout type = new HorizontalLayout();
        Label typeLable = new Label(getTranslation("search.type"));
        String typeString = "";
        if (product.getType() == 0) {
            typeString = getTranslation("product.type.none");
        } else if (product.getType() == 1) {
            typeString = getTranslation("product.type.decl");
        } else {
            typeString = getTranslation("product.type.cert");
        }
        Label typeInfo = new Label(typeString);
        typeLable.setWidth(LABLE_WIDTH);
        typeInfo.setWidth(INFO_WIDTH);
        typeInfo.getStyle().set("font-size", FONT_SIZE);
        type.add(typeLable, typeInfo);
        HorizontalLayout size = new HorizontalLayout();
        Label sizeLable = new Label(getTranslation("product.certs.size"));
        Label sizeInfo = new Label(product.getCertificates().size() + "");
        sizeLable.setWidth(LABLE_WIDTH);
        sizeInfo.setWidth(INFO_WIDTH);
        sizeInfo.getStyle().set("font-size", FONT_SIZE);
        size.add(sizeLable, sizeInfo);
        Button delite = new Button();
        com.gmail.breninsul.jd2.pojo.Image prodImage = product.getImage();
        StreamResource resource = null;
        if (prodImage == null) {
            resource = new StreamResource(new Random().nextInt() + ".jpg", () -> (getClass().getClassLoader().getResourceAsStream("default_product.jpg")));
        } else {
            if (prodImage.getImg() == null) {
                resource = new StreamResource(new Random().nextInt() + ".jpg", () -> (getClass().getClassLoader().getResourceAsStream("default_product.jpg")));
            } else {
                resource = new StreamResource(new Random().nextInt() + ".jpg", () -> new ByteArrayInputStream(prodImage.getImg()));
            }
        }
        com.vaadin.flow.component.html.Image image = new Image(resource, new Random().nextInt() + "");
        delite.setText(getTranslation("product.button.delite"));
        Div div = new Div(image);
        image.setHeight(IMAGE_SIZE);
        image.setWidth(IMAGE_SIZE);
        image.getStyle().set("min-width", IMAGE_SIZE);
        image.getStyle().set("min-width", IMAGE_SIZE);
        image.getStyle().set("min-height", IMAGE_SIZE);
        image.getStyle().set("overflow", "visible");
        Button addCert = new Button(getTranslation("product.add.cert"));
        VerticalLayout buttons = new VerticalLayout(addCert, delite);
        HorizontalLayout imageAndButtons = new HorizontalLayout(image, buttons);
        log.info("have to add cert=" + haveToAddCert);
        addCert.setVisible(haveToAddCert);
        add(name, manufacturer, type, size, imageAndButtons);
        setHeight(BOX_HEIGHT);
        setWidth(BOX_WIDTH);
        int id = product.getId();
        delite.addClickListener(e -> {
            productService.delete(product);
            UI.getCurrent().getPage().reload();
        });
        addCert.addClickListener(e -> {
            wantedCertificate.setProduct(product);
            VaadinSession.getCurrent().setAttribute("WantedCert", null);
            certificateService.save(wantedCertificate);
            UI.getCurrent().getPage().reload();

        });
        addClickListener(e -> {
            Dialog dialog = new Dialog();
            dialog.setCloseOnEsc(true);
            dialog.setCloseOnOutsideClick(true);
            Grid<Certificate> grid = new Grid<>();
            dialog.add(inintEditor(product, productService));
            dialog.add(new H3(getTranslation("cert.delite.adv")));
            dialog.add(grid);

            grid.setItems(product.getCertificates());
            grid.addColumn(Certificate::getProductName).setHeader(getTranslation("search.name")).setResizable(true);
            grid.addColumn(Certificate::getNumber).setHeader(getTranslation("search.number")).setResizable(true);
            grid.addColumn(Certificate::getManufacturer).setHeader(getTranslation("search.manufacturer")).setResizable(true);
            grid.addColumn(Certificate::getApplicant).setHeader(getTranslation("search.applicant")).setResizable(true);
            grid.addColumn(Certificate::getStringCertType).setHeader(getTranslation("cert.type")).setResizable(true);
            grid.addColumn(Certificate::getSimpleBeginDate).setHeader(getTranslation("search.begindate")).setResizable(true);
            grid.addColumn(Certificate::getSimpleEndDate).setHeader(getTranslation("search.enddate")).setResizable(true);
            grid.addComponentColumn(item -> createRemoveButton(grid, item, certificateService))
                    .setHeader(getTranslation("delite"));
            grid.setHeightByRows(true);
            dialog.setWidth(DIALOG_WIDTH);
            dialog.setHeight(DIALOG_HEIGHT);
            dialog.open();
        });
    }

    private Button createRemoveButton(Grid<Certificate> grid, Certificate item, CertificateService certificateService) {
        Button button = new Button(getTranslation("delite"), clickEvent -> {
            ListDataProvider<Certificate> dataProvider = (ListDataProvider<Certificate>) grid
                    .getDataProvider();
            dataProvider.getItems().remove(item);
            item.setProduct(null);
            certificateService.delete(item);
            dataProvider.refreshAll();
            UI.getCurrent().getPage().reload();
        });
        return button;
    }

    private Div inintEditor(Product product, ProductService productService) {
        Binder<Product> binder = new Binder<Product>(Product.class);
        Div dialog = new Div();
        Button save = new Button();
        VerticalLayout nameLayout = new VerticalLayout();
        //FormLayout nameLayout = new FormLayout();
        dialog.add(nameLayout);
        TextField prodNameField = new TextField();
        prodNameField.setLabel(getTranslation("product.name"));
        prodNameField.setWidth(FIELD_WIDTH);
        prodNameField.setRequired(true);
        //prodNameField.setValue(product.getProductName());
        Binder.Binding<Product, String> nameBind = binder.forField(prodNameField)
                .bind(Product::getProductName, Product::setProductName);
        nameLayout.add(prodNameField);
        TextField manufacturerField = new TextField();
        manufacturerField.setLabel(getTranslation("product.manufacturer"));
        manufacturerField.setWidth(FIELD_WIDTH);
        manufacturerField.setRequired(true);
        Binder.Binding<Product, String> manufacturerBinding = binder.forField(manufacturerField)
                .bind(Product::getManufacturer, Product::setManufacturer);
        nameLayout.add(manufacturerField);
        Select<String> typesList = new Select<>();
        //Select<String> typesList = new Select<>();
        List<String> types = new ArrayList<>();
        for (int i = 0; i < 47; i++) {
            types.add((ProductTypes.get(i)));
        }
        typesList.setItems(types);
        typesList.setValue(types.get(0));
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setMaxFiles(1);
        com.gmail.breninsul.jd2.pojo.Image image = new com.gmail.breninsul.jd2.pojo.Image();
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setDropLabel(new Label(getTranslation("product.image.upload")));
        upload.addSucceededListener(event -> {
            image.setImg(buffer.getInputStream(event.getFileName()));
            log.info("img uploaded" + image.getImg().toString());
        });
        typesList.setLabel(getTranslation("product.type"));
        nameLayout.add(typesList);
        nameLayout.add(upload);
        nameLayout.add(save);
        binder.readBean(product);
        save.setIcon(VaadinIcon.PLUS.create());
        save.setText(getTranslation("product.save"));
        save.addClickListener(e -> {
            if (binder.writeBeanIfValid(product)) {
                product.setImage(image);
                productService.save(product);
                Notification.show(getTranslation("product.saved"), TIMEOUT, Notification.Position.MIDDLE);
                UI.getCurrent().getPage().reload();
            }
        });
        return dialog;
    }
}

