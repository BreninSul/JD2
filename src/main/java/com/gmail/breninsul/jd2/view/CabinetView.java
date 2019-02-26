package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.config.ProductTypes;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Image;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import com.gmail.breninsul.jd2.service.CertificateService;
import com.gmail.breninsul.jd2.service.ProductService;
import com.gmail.breninsul.jd2.service.UserService;
import com.gmail.breninsul.jd2.view.menu.Menu;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.SpringVaadinSession;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gmail.breninsul.jd2.view.LoginView.TIMEOUT;

@Log4j2
@Route(value = "cabinet", layout = Menu.class)
public class CabinetView extends VerticalLayout {
    public static final String WIDTH = "90vw";
    public static final String DIALOG_WIDTH = "48vw";
    public static final String DIALOG_HEIGHT = "60vh";
    public static final String FIELD_WIDTH = "20vw";
    public static final String DIV_WIDTH = "48vw";
    public static final int PAGINATION_SIZE = 9;
    private Binder<Product> binder = new Binder<Product>(Product.class);
    private Integer page = 0;
    private Div resultDiv = new Div();

    private Component createComponent(String mimeType, String fileName,
                                      InputStream stream) {
        if (!mimeType.startsWith("text")) {
            throw new IllegalStateException();
        }
        String text = "";
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        Div div = new Div();
        div.setText(text);
        div.addClassName("uploaded-text");
        return div;
    }

    public CabinetView(UserService userService, ProductService productService, CertificateService certificateService) {
        String sort = null;
        String field = null;
        User user = (User) VaadinSession.getCurrent().getAttribute("OurUser");
                //userService.getUserByName(ViewUtils.getUsername());
        Button addProduct = new Button(getTranslation("cabinet.product.add"));
        HorizontalLayout addProdLayout = new HorizontalLayout();
        HorizontalLayout sortLayout = new HorizontalLayout();
        sortLayout.setVerticalComponentAlignment(Alignment.CENTER);
        Select<String> sortType = new Select();
        sortType.setLabel(getTranslation("sort.field.ch"));
        sortType.setItems("DESC", "ASC");
        sortType.setRenderer(new ComponentRenderer<>(e -> {
            Div text = new Div();
            text.setText(getTranslation(e));
            FlexLayout wrapper = new FlexLayout();
            wrapper.setFlexGrow(1, text);
            wrapper.add(text);
            return wrapper;
        }));
        sortType.setValue("DESC");
        Select<String> sortField = new Select();
        sortField.setLabel(getTranslation("sort.type.ch"));
        sortField.setItems("updatedDate", "createdDate");
        sortField.setRenderer(new ComponentRenderer<>(e -> {
            Div text = new Div();
            text.setText(getTranslation(e));
            FlexLayout wrapper = new FlexLayout();
            wrapper.setFlexGrow(1, text);
            wrapper.add(text);
            return wrapper;
        }));
        sortField.setValue("updatedDate");
        sortType.addValueChangeListener(e -> {
            initResults(userService, productService, certificateService, user, page, sortType.getValue(), sortField.getValue());
        });
        sortField.addValueChangeListener(e -> {
            initResults(userService, productService, certificateService, user, page, sortType.getValue(), sortField.getValue());
        });
        sortLayout.add(new EmptyDiv(),new Label(getTranslation("cabinet.sort.ch")), sortType, sortField,new Label(getTranslation("cabinet.page.ch")));
        int productsSize = productService.countProductsByUserId(user.getId());
        int pages =((Double) Math.ceil((double) (productsSize / PAGINATION_SIZE))).intValue();
        List <Button> buttons=new ArrayList<>();
        for (int i = 0; i < pages+1; i++) {
            H4 h4 = new H4(i+1 + "");
            Button button=new Button(i+1+"");
            buttons.add(button);
            button.addThemeVariants(ButtonVariant.LUMO_SMALL);
            if (i==0){
                button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
            }
            button.addClickListener(e -> {
                int page= NumberUtils.toInt(h4.getText())-1;
                initResults(userService, productService, certificateService, user, page, sortField.getValue(), sortField.getValue());
                for (Button b:buttons                     ) {
                    try {
                        b.removeThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
                    } catch (Exception e1){

                    }
                }
                button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
            });
            sortLayout.add(button);
        }
        add(sortLayout);
        addProdLayout.add(new EmptyDiv(), addProduct);
        add(addProdLayout);
        Button save = new Button();
        Dialog dialog = new Dialog();
        VerticalLayout nameLayout = new VerticalLayout();
        dialog.add(nameLayout);
        TextField prodNameField = new TextField();
        prodNameField.setLabel(getTranslation("product.name"));
        prodNameField.setWidth(FIELD_WIDTH);
        prodNameField.setRequired(true);
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
        typesList.setLabel(getTranslation("product.type"));
        List<String> types = new ArrayList<>();
        for (int i = 0; i < 47; i++) {
            types.add((ProductTypes.get(i)));
        }

        typesList.setItems(types);
        typesList.setValue(types.get(0));
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setMaxFiles(1);
        Image image = new Image();
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setDropLabel(new Label(getTranslation("product.image.upload")));
        upload.addSucceededListener(event -> {
            image.setImg(buffer.getInputStream(event.getFileName()));
            log.info("img uploaded" + image.getImg().toString());
        });
        nameLayout.add(typesList);
        nameLayout.add(upload);
        nameLayout.add(save);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        addProduct.addClickListener(e -> {
            dialog.open();
        });
        save.setIcon(VaadinIcon.PLUS.create());
        save.setText(getTranslation("product.save"));

        save.addClickListener(e -> {
            Product product = new Product();
            Image img=new Image();
            img.setImg(image.getAsBufferedImage());
            img.setImg(image.getAsBufferedImage());
            if (binder.writeBeanIfValid(product)) {
                product.setImage(img);
                product.setUser(user);
                productService.save(product);
                Notification.show(getTranslation("product.saved"), TIMEOUT, Notification.Position.MIDDLE);
                initResults(userService, productService, certificateService, user, page, sortType.getValue(), sortField.getValue());
            }
            dialog.close();
        });
        log.info("Sort field="+sortField.getValue());
        initResults(userService, productService, certificateService, user, page, sortType.getValue(), sortField.getValue());
        add(resultDiv);
    }

    private void initResults(UserService userService, ProductService productService, CertificateService certificateService, User user, int page, String sortType, String field) {
        resultDiv.removeAll();
        List<Product> products = null;
        log.info("Sort by="+sortType+" field="+field);
        if (!("DESC".equals(sortType) ||"ASC".equals(sortType))){
            sortType=null;
        }
        if (!("updatedDate".equals(field) ||"createdDate".equals(field))){
            field=null;
        }
        if (sortType == null) {
            products = userService.getProductsPagedAndSorted(user, PAGINATION_SIZE, page);
        } else {
            if (field == null) {
                products = userService.getProductsPagedAndSorted(user, sortType, PAGINATION_SIZE, page);
            } else {
                products = userService.getProductsPagedAndSorted(user, sortType, field, PAGINATION_SIZE, page);
            }
        }
        log.info(products.toString());
        List<HorizontalLayout> layouts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            layouts.add(new HorizontalLayout());
            for (int j = 0; j < 3; j++) {
                int n = i * 3 + j;
                if (n < products.size()) {
                    layouts.get(i).add(new ProductBox(certificateService, productService, products.get(n)));
                } else {
                    break;
                }
            }
        }
        VerticalLayout result = new VerticalLayout();
        for (HorizontalLayout l : layouts) {
            result.add(l);
        }
        resultDiv.add(result);
    }
}
