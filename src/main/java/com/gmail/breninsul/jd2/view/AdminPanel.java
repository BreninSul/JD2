package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.pojo.User;
import com.gmail.breninsul.jd2.service.UserService;
import com.gmail.breninsul.jd2.service.impl.UserDetailsServiceImpl;
import com.gmail.breninsul.jd2.view.menu.Menu;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

import static com.gmail.breninsul.jd2.view.LoginView.TIMEOUT;

@Log4j2
@Route(value = "panel", layout = Menu.class)
public class AdminPanel extends VerticalLayout {
    private Grid<User> grid = new Grid<>();
    private TextField filter = new TextField();
    private TextField nameField = new TextField();
    private TextField idField = new TextField();
    private TextField passField = new TextField();
    private TextField roleField = new TextField();


    private TextField mailField = new TextField();

    private UserService userService;
    private Binder<User> binder = new Binder<>(User.class);
    private Editor<User> editor = grid.getEditor();

    private void initGrid() {
        List<User> users = userService.findAll();
        grid.setItems(users);
        grid.setHeightByRows(true);
        initIdColumn();
        initEditorColumn();
        initEnabledColumn();
        initMailColumn();
        initPassColumn();
        initRoleColumn();
        initNameColumn();
        initFilter();
    }

    private void initEditorColumn() {
        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());
        Grid.Column<User> editorColumn = grid.addComponentColumn(user -> {
            Button edit = new Button("Edit");
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                editor.editItem(user);
                nameField.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });
        editor.addOpenListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        Button save = initSaveButton(idField);
        Button cancel = initCancelButton(editor);
        Div buttons = new Div(save, cancel);
        editorColumn.setEditorComponent(buttons);
        editor.setBinder(binder);
        editor.setBuffered(true);
        editor.addSaveListener(
                event -> {
                    log.info(event.toString());
                });
    }

    private void initNameColumn() {
        Grid.Column<User> nameColumn = grid.addColumn(User::getName)
                .setHeader(getTranslation("user.name"));
        binder.forField(nameField)
                .bind("name");
        nameColumn.setEditorComponent(nameField);
    }

    private void initEnabledColumn() {
        Grid.Column<User> enabledColumn = grid
                .addColumn(User::isEnabled).setHeader(getTranslation("user.enabled"));
        Checkbox checkbox = new Checkbox();
        binder.bind(checkbox, "enabled");
        enabledColumn.setEditorComponent(checkbox);
    }

    private void initRoleColumn() {
        Grid.Column<User> roleColumn = grid.addColumn(User::getRole)
                .setHeader(getTranslation("user.role"));
        Div validationStatus = new Div();
        validationStatus.setId("validation");
        binder.forField(roleField)
                .withValidator(v -> {
                    return UserDetailsServiceImpl.UserRoleEnum.ROLE_USER.name().equals(v) || UserDetailsServiceImpl.UserRoleEnum.ROLE_ADMIN.name().equals(v);
                }, getTranslation("wrong.role.warning"))
                .withStatusLabel(validationStatus).bind("role");
        roleColumn.setEditorComponent(roleField);
    }

    private void initPassColumn() {
        Grid.Column<User> passColumn = grid.addColumn(User::getPass)
                .setHeader(getTranslation("user.pass"));
        binder.forField(passField)
                .bind("pass");
    }

    private void initMailColumn() {
        Grid.Column<User> mailColumn = grid.addColumn(User::getEmail)
                .setHeader(getTranslation("user.mail"));
        binder.forField(mailField)
                .bind("email");
        mailColumn.setEditorComponent(mailField);
    }

    private void initIdColumn() {
        Grid.Column<User> idColumn = grid.addColumn(User::getId)
                .setHeader("ID");
        binder.forField(idField)
                .withConverter(new StringToIntegerConverter("Please insert a number"))
                .bind("id");

    }

    private Button initCancelButton(Editor<User> editor) {
        Button cancel = new Button("Cancel", e -> editor.cancel());
        cancel.addClassName("cancel");
        cancel.addClickListener(c -> {
            editor.cancel();
        });
        return cancel;
    }

    private Button initSaveButton(TextField idField) {
        Button save = new Button("Save", e -> {
            editor.save();
            User userFromDB = userService.getOne(NumberUtils.toInt(idField.getValue()));
            User user = new User();
            if (binder.writeBeanIfValid(user)) {
                userFromDB.setRole(user.getRole());
                userFromDB.setName(user.getName());
                userFromDB.setEmail(user.getEmail());
                userService.save(userFromDB);
                UI.getCurrent().getPage().reload();
            } else {
                Notification.show(getTranslation("wrong.role.warning"), TIMEOUT, Notification.Position.MIDDLE);
            }
        });
        save.addClassName("save");
        return save;
    }

    private void initFilter() {
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> {
            if (e.getValue().isEmpty()) {
                grid.setItems(userService.getUserByName(e.getValue()));
            } else {
                User user = userService.getUserByName(e.getValue());
                List userL = new ArrayList();
                if (user != null) {
                    userL.add(user);
                }
                grid.setItems(userL);
            }
        });
    }

    ;

    public AdminPanel(UserService userService) {
        this.userService = userService;
        filter.setPlaceholder(getTranslation("filter.by.name"));
        add(filter);
        initGrid();
        add(grid);
    }
}
