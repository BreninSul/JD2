package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.pojo.User;
import com.gmail.breninsul.jd2.service.UserService;
import com.gmail.breninsul.jd2.service.impl.UserDetailsServiceImpl;
import com.gmail.breninsul.jd2.view.menu.Menu;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
@Route(value = "login", layout = Menu.class)
public class LoginView extends DivWithLoginFunction {
    public static final String DIALOG_WIDTH = "24vw";
    public static final String DIALOG_HEIGHT = "60vh";
    public static final String DIALOG_WIDTH_L = "48vw";
    public static final String FIELD_WIDTH = "20vw";
    public static final String URL = "http://127.0.0.1:8080/login/validate?id=";
    public static final int TIMEOUT = 10000;
    private User user = new User();
    private Binder<User> binder = new Binder<User>(User.class);

    private static final String MAIL_PATTERN = "[a-zA-Z][a-zA-Z0-9]*[@][a-zA-Z0-9]*[.][a-zA-Z0-9]*";
    private static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9]*";
    private FormLayout form = new FormLayout();
    private Dialog dialog = new Dialog();
    private Button loginButton = new Button();
    private Label title = new Label();
    private Label errorMsg = new Label();
    private Button regButton = new Button();
    private PasswordField secondPasswordField = new PasswordField();
    private VerticalLayout regLayout = new VerticalLayout();
    private TextField userNameField = new TextField();
    private TextField mailField = new TextField();
    private Checkbox checkbox = new Checkbox();
    private PasswordField passwordField = new PasswordField();
    private VerticalLayout loginLayout = new VerticalLayout();


    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    protected UserService userService;

    private TextField initUserField() {
        userNameField.setLabel(getTranslation("login.username"));
        userNameField.setPlaceholder(getTranslation("login.username.placeholder"));
        userNameField.setMaxLength(200);
        userNameField.setMinLength(1);
        userNameField.setErrorMessage(getTranslation("login.user.warning"));
        userNameField.setWidth(FIELD_WIDTH);
        userNameField.setRequired(true);
        Binder.Binding<User, String> usernameBinding = binder.forField(userNameField)
                .withValidator(name -> {
                    return name.matches(USERNAME_PATTERN);
                }, getTranslation("login.user.warning"))
                .bind(User::getName, User::setName);
        userNameField.addValueChangeListener(event -> {
            usernameBinding.validate();
        });
        return userNameField;
    }

    private TextField initMailField() {
        mailField.setLabel(getTranslation("login.mail.lable"));
        mailField.setPlaceholder(getTranslation("login.mail"));
        mailField.setMaxLength(200);
        mailField.setMinLength(1);
        mailField.setErrorMessage(getTranslation("login.mail.warning"));
        mailField.setWidth(FIELD_WIDTH);
        Binder.Binding<User, String> emailBinding = binder.forField(mailField)
                .withValidator(new EmailValidator(getTranslation("login.mail.warning")))
                .bind(User::getEmail, User::setEmail);
        mailField.addValueChangeListener(event -> {
                    emailBinding.validate();
                }
        );
        return mailField;
    }

    private Checkbox initRegisterCheckbox() {
        checkbox.setLabel(getTranslation("login.new.user"));
        checkbox.addValueChangeListener(event -> {
            if (event.getValue()) {
                dialog.setWidth(DIALOG_WIDTH_L);
                regLayout.setVisible(true);
                secondPasswordField.setRequired(true);
                regButton.setVisible(true);
                loginButton.setEnabled(false);
            } else {
                dialog.setWidth(DIALOG_WIDTH);

                regLayout.setVisible(false);
                secondPasswordField.setRequired(false);
                regButton.setVisible(false);
                loginButton.setEnabled(true);
            }
        });
        return checkbox;
    }

    private PasswordField initPasswordField() {
        passwordField.setLabel(getTranslation("login.pass"));
        passwordField.setRequired(true);
        passwordField.setWidth(FIELD_WIDTH);
        Binder.Binding<User, String> passBinding = binder.forField(passwordField)
                .bind(User::getPass, User::setPass);
        return passwordField;
    }

    private PasswordField initSecondPasswordField() {
        secondPasswordField.setLabel(getTranslation("login.pass.second"));
        secondPasswordField.setRequired(false);
        secondPasswordField.setWidth(FIELD_WIDTH);
        return secondPasswordField;
    }

    private Button initLoginButton() {
        Icon icon = VaadinIcon.ENTER_ARROW.create();
        icon.setColor("GREEN");
        loginButton.setText(getTranslation("login.loginbutton"));
        loginButton.setIcon(icon);
        loginButton.setIconAfterText(true);
        loginButton.addClickListener(event -> {
            List<HasValue> fields = binder.getFields().collect(Collectors.toList());
            performLogin(userNameField.getValue().toString(), passwordField.getValue().toString());
            log.info("Closing login form");
            if (ViewUtils.isLoggined()) {
                User user = userService.getUserByName(ViewUtils.getUsername());
                VaadinSession.getCurrent().setAttribute("OurUser",user);
                // UI.getCurrent().getSession().setAttribute("user",userService.getUserByName(userNameField.getValue()));
                dialog.close();
                UI.getCurrent().navigate(CabinetView.class);
            } else {
                errorMsg.setVisible(true);
            }
        });
        return loginButton;
    }

    private Button initRegButton(String route) {
        Icon icon = VaadinIcon.PLUS.create();
        icon.setColor("GREEN");
        regButton.setText(getTranslation("login.regbutton"));
        regButton.setIcon(icon);
        regButton.setVisible(false);
        regButton.addClickListener(event -> {
            if (passwordField.getValue().equals( secondPasswordField.getValue())) {
                log.info(binder.isValid());
                if (binder.writeBeanIfValid(user)) {
                    boolean uniqName = userService.getUserByName(user.getName()) == null;
                    log.info("Checking if user with the same name already exists =" + !uniqName);
                    if (uniqName) {
                        user.setRole(UserDetailsServiceImpl.UserRoleEnum.ROLE_USER.name());
                        userService.save(user);
                        sendMail(user.getEmail(), user.getId(), user.hashCode());
                        Notification.show(getTranslation("login.email.sent"), TIMEOUT, Notification.Position.MIDDLE);
                        dialog.close();
                        add(new H1(getTranslation("login.email.sent")));
                    } else {
                        Notification.show(getTranslation("login.usename.exists"), TIMEOUT, Notification.Position.MIDDLE);
                    }
                } else {
                    BinderValidationStatus<User> validate = binder.validate();
                    String errorText = validate.getFieldValidationStatuses()
                            .stream().filter(BindingValidationStatus::isError)
                            .map(BindingValidationStatus::getMessage)
                            .map(Optional::get).distinct()
                            .collect(Collectors.joining(", "));
                    log.info("There are errors: " + errorText);
                    Notification.show(errorText, TIMEOUT, Notification.Position.MIDDLE);

                }
            } else {
                Notification.show(getTranslation("login.password.not.same"), TIMEOUT, Notification.Position.MIDDLE);
                errorMsg.setVisible(true);
            }
        });
        return regButton;
    }

    private void sendMail(String mail, int id, int hash) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail);
        message.setSubject("Registration");
        message.setText(URL+ id + "&hash=" + hash);
        emailSender.send(message);
    }

    private VerticalLayout initReg() {
        regLayout.add(mailField, secondPasswordField);
        regLayout.setVisible(false);
        return regLayout;
    }

    private VerticalLayout initLogin() {
        errorMsg.setText(getTranslation("login.wron.usernameorpass"));
        errorMsg.setVisible(false);
        errorMsg.getStyle().set("color", "red");
        loginLayout.add(userNameField, passwordField, checkbox, errorMsg, loginButton);
        return loginLayout;
    }

    public LoginView(UserService service) {
        String route = UI.getCurrent().getRouter()
                .getUrl(MainView.class);
        if (ViewUtils.isLoggined()) {
            UI.getCurrent().navigate(CabinetView.class);
        }
        initMailField();
        initPasswordField();
        initSecondPasswordField();
        initUserField();
        initRegisterCheckbox();
        initLoginButton();
        initRegButton(route);
        initLogin();
        initReg();
        dialog.add(form);
        title.setText(getTranslation("login.log.title"));
        HorizontalLayout actions = new HorizontalLayout(loginLayout, regLayout);
        HorizontalLayout buttons = new HorizontalLayout(loginButton, regButton);
        VerticalLayout main = new VerticalLayout(actions, buttons);
        form.add(main);
        dialog.setWidth(DIALOG_WIDTH);
        dialog.setHeight(DIALOG_HEIGHT);
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnOutsideClick(true);
        dialog.setCloseOnEsc(true);
        dialog.getElement().addEventListener("opened-changed", e -> {
            log.info("Closing login form");
            if (ViewUtils.isLoggined()) {
                UI.getCurrent().getPage().reload();
                UI.getCurrent().navigate(CabinetView.class);
            } else {
                UI.getCurrent().navigate(SearchView.class);
            }
        });
        dialog.open();
    }

}
