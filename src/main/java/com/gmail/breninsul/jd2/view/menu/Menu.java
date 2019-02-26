package com.gmail.breninsul.jd2.view.menu;

import com.gmail.breninsul.jd2.pojo.User;
import com.gmail.breninsul.jd2.service.SecurityService;
import com.gmail.breninsul.jd2.service.UserService;
import com.gmail.breninsul.jd2.service.impl.SecurityServiceImpl;
import com.gmail.breninsul.jd2.service.impl.UserDetailsServiceImpl;
import com.gmail.breninsul.jd2.view.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.annotation.WebInitParam;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.gmail.breninsul.jd2.view.LoginView.TIMEOUT;

@Log4j2
@WebInitParam(name = Constants.I18N_PROVIDER, value = "com.gmail.breninsul.jd2.view.i18")
public class Menu extends Composite<Div> implements RouterLayout {
    private static final double ICON_SIZE_NUM = 2.5;
    private static final double MARGIN_NUM = 0.8;
    private static final String ICON_SIZE = ICON_SIZE_NUM + "vw";
    private static final String BUTTON_HEIGHT = ICON_SIZE_NUM + 1.0 + "vw";
    private static final String ICON_COLOR = "blue";
    public static final String FLAG_USER_ICON_SIZE = "20px";
    private final String MENU_TABS_HEIGHT = ICON_SIZE_NUM * 2 + "vw";
    public static final String RB_FLAG_PATH = "frontend/RBFlag.svg";
    public static final String RF_FLAG_PATH = "frontend/RFFlag.svg";
    public static final String UK_FLAG_PATH = "frontend/UKFlag.svg";

    private static final Map<String, String> ICON_STYLE = new HashMap<String, String>() {{
        put("position", "absolute");
        put("top", MARGIN_NUM + "vw");
        put("right", MARGIN_NUM + "vw");
        put("z-index", "99999999");
    }};
    private Tabs tabs;
    private Icon userIcon = new Icon(VaadinIcon.USER);
    private Label loginUsernameLabel = new Label();
    private Label loginMassageLabel = new Label();
    private Image ruFlag = getFlagImage(RF_FLAG_PATH, "ru");
    private Image rbFlag = getFlagImage(RB_FLAG_PATH, "by");
    private Image ukFlag = getFlagImage(UK_FLAG_PATH, "en");
    private Image actualLangIcon = setLangIcon();
    private Select<Image> select = new Select<>();

    // private Label loginLabel = new Label();
    private Div loginLable = new Div(loginUsernameLabel, new Hr(), loginMassageLabel);
    @Autowired
    private DaoAuthenticationProvider authenticationProvider;
    @Autowired
    private UserDetailsService userDetailsServiceImpl;
    @Autowired
    SecurityServiceImpl securityServiceImpl;
    @Autowired
    private AuthenticationManager authenticationManager;

    private Button initHideButton() {
        Icon icon = VaadinIcon.MENU.create();
        Button buttonComponent = new Button(icon);
        Style buttonStyle = buttonComponent.getStyle();
        Style iconStyle = icon.getStyle();
        icon.setColor(ICON_COLOR);
        icon.setSize(ICON_SIZE);
        for (Map.Entry<String, String> e : ICON_STYLE.entrySet()) {
            buttonStyle.set(e.getKey(), e.getValue());
        }
        buttonComponent.setWidth(BUTTON_HEIGHT);
        buttonComponent.setHeight(BUTTON_HEIGHT);
        buttonComponent.addClickListener(event -> {
            ViewUtils.hideOrUnhideComponent(tabs);
        });
        return buttonComponent;
    }


    private Image getFlagImage(String path, String lang) {
        Image img = new Image();
        img.setSrc(path);
        img.setHeight(FLAG_USER_ICON_SIZE);
        img.setWidth(FLAG_USER_ICON_SIZE);
        img.setText(getTranslation("menu.lang." + lang));
        img.setId(lang);
        return img;
    }


    public void setUserIconColourAndText() {
        loginUsernameLabel.setText(getTranslation("menu.logged.out"));
        String name = ViewUtils.getUsername();
        if (name != null) {
            userIcon.setColor("GREEN");
            loginUsernameLabel.setText(name);
            loginMassageLabel.setText(getTranslation("menu.logout"));
        } else {
            userIcon.setColor("RED");
            loginMassageLabel.setText(getTranslation("menu.login"));
        }
    }

    public Image setLangIcon() {
        actualLangIcon = new Image();
        actualLangIcon.setVisible(true);
        actualLangIcon.setWidth(FLAG_USER_ICON_SIZE);
        actualLangIcon.setHeight(FLAG_USER_ICON_SIZE);
        String lang = getLocale().getLanguage();
        log.info("Setting locale flag = " + lang);
        actualLangIcon.setSrc(ukFlag.getSrc());
        actualLangIcon.setText(getTranslation("menu.lang"));
        if ("ru".equals(lang)) {
            actualLangIcon.setSrc(ruFlag.getSrc());
            return actualLangIcon;
        }
        if ("by".equals(lang)) {
            actualLangIcon.setSrc(rbFlag.getSrc());
            return actualLangIcon;
        }
        return actualLangIcon;
    }

    private Tab initLoginTab() {
        Tab tab = new Tab(loginLable);
        tab.add(userIcon);
        tab.add();
        tab.getElement().addEventListener("click", e -> {
            setUserIconColourAndText();
            if (ViewUtils.isLoggined()){
                Authentication authenticated=null;
                UI.getCurrent().getPage().reload();
                SecurityContextHolder.clearContext();
            } else {
                UI.getCurrent().navigate(LoginView.class);
            }
        });
        setUserIconColourAndText();
        return tab;
    }

    private Tab initLangChanger() {
        select.addToPrefix(actualLangIcon);
        select.setEmptySelectionCaption(actualLangIcon.getText());
        select.setEmptySelectionAllowed(true);
        select.setItems(ruFlag, rbFlag, ukFlag);
        select.addToPrefix(actualLangIcon);
        VaadinSession session = UI.getCurrent().getSession();
        select.addValueChangeListener(e -> {
            try {
                Locale locale = new Locale(e.getValue().getId().get());
                log.info("Changing locale to " + locale);
                session.setLocale(locale);
                UI.getCurrent().getPage().reload();
            } catch (Exception ex) {
                log.info("Changing locale failed");
                log.info(ex);
            }
        });
        select.setRenderer(new ComponentRenderer<>(e -> {
            Div text = new Div();
            text.setText(e.getText());
            FlexLayout wrapper = new FlexLayout();
            wrapper.setFlexGrow(1, text);
            wrapper.add(e, text);
            return wrapper;
        }));
        Tab tab = new Tab();
        tab.add(select);
        return tab;
    }

    private Tab initSearch() {
        Tab search = (new Tab(getTranslation("menu.search")));
        search.getElement().addEventListener("click", e -> {
            UI.getCurrent().navigate(SearchView.class);
        });
        return search;
    }
    private Tab initCabinet() {
        Tab cabinet = (new Tab(getTranslation("menu.cabinet")));
        cabinet.getElement().addEventListener("click", e -> {
            UI.getCurrent().navigate(CabinetView.class);
        });
        return cabinet;
    }
    private Tab initAdminPanel() {
        Tab panel = (new Tab(getTranslation("menu.panel")));
        panel.getElement().addEventListener("click", e -> {
            UI.getCurrent().navigate(AdminPanel.class);
        });
        return panel;
    }
    private Tab initLog() {
        Tab log = (new Tab(getTranslation("menu.log")));
        log.getElement().addEventListener("click", e -> {
            UI.getCurrent().navigate(LogView.class);
        });
        return log;
    }
    private Div initMenuTabs() {

        Tab login = initLoginTab();
        Tab langCahnge = initLangChanger();
        Tab search = initSearch();
        Tab cabinet = initCabinet();
        Tab adminPanel=initAdminPanel();
        Tab logV=initLog();
        boolean roleUser=false;
        boolean roleAdmin=false;
        User user= (User) VaadinSession.getCurrent().getAttribute("OurUser");
        if (user!=null){
            if (UserDetailsServiceImpl.UserRoleEnum.ROLE_USER.name().equals(user.getRole())){
                roleUser=true;
            } else if (UserDetailsServiceImpl.UserRoleEnum.ROLE_ADMIN.name().equals(user.getRole())){
                roleUser=true;
                roleAdmin=true;
            }
        }
        cabinet.setVisible(roleUser);
        adminPanel.setVisible(roleAdmin);
        logV.setVisible(roleAdmin);
        tabs = new Tabs(login, search,cabinet, adminPanel,logV,langCahnge);
        tabs.setSelectedTab(search);
        tabs.setHeight(MENU_TABS_HEIGHT);
        tabs.setFlexGrowForEnclosedTabs(1);
        return new Div(tabs);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        setUserIconColourAndText();
        setLangIcon();
    }

    public Menu(UserService userService) {
       if (ViewUtils.isLoggined()){
           if(VaadinSession.getCurrent().getAttribute("OurUser")==null){
               log.info("there is no user in session< putting it");
               User user = userService.getUserByName(ViewUtils.getUsername());
               VaadinSession.getCurrent().setAttribute("OurUser",user);
           }
       }
       VaadinSession.getCurrent().setErrorHandler(new ErrorHandler() {
            @Override
            public void error(ErrorEvent event) {
                log.info("",event.getThrowable());
                Notification.show(getTranslation("error.network"), TIMEOUT, Notification.Position.MIDDLE);
                UI.getCurrent().navigate(SearchView.class);
            }
        });
        Button button = initHideButton();
        Div tabs = initMenuTabs();
        getContent().add(button);
        getContent().add(new Div(tabs));
        VaadinResponse resp = VaadinService.getCurrentResponse();
        VaadinRequest req = VaadinService.getCurrentRequest();
        //log.info("Locale=" + req.getLocale());
        //log.info("Cookies= " + req.getCookies().toString());
        //log.info(UI.getCurrent().getLocale());
    }

}
