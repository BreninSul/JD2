package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.pojo.User;
import com.gmail.breninsul.jd2.service.UserService;
import com.gmail.breninsul.jd2.view.menu.Menu;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Log4j2
@Route(value = "login/validate", layout = Menu.class)
public class Validator extends DivWithLoginFunction {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    public static final int TIMEOUT_MILLIS = 5000;

    public Validator(UserService userService) {
        int id = NumberUtils.toInt(VaadinService.getCurrentRequest().getParameter("id"));
        int reqHash = NumberUtils.toInt(VaadinService.getCurrentRequest().getParameter("hash"));
        log.info(id + " trying to validate");
        User user = null;
        int userHash = 0;
        try {
            user = userService.getOne(id);
            userHash = user.hashCode();
        } catch (Exception e) {
            log.info("It seems that there is no user with id=" + id);
            redirectToLogin();
        }
        if (reqHash == userHash) {
            user.setEnabled(true);
            userService.save(user);
            log.info("User with id=" + id + " has been validated");
            add(new H1(getTranslation("validate.sucsess1") + user.getName() + getTranslation("validate.sucsess2")));
            redirectToLogin();
        } else {
            add(new H1(getTranslation("validate.error")));
        }
        try {
            Thread.sleep(TIMEOUT_MILLIS) ;
        } catch (InterruptedException e) {
           log.info("Something happend with thread, no matter");
        } finally {
           // redirectToLogin();
        }
    }

    private void redirectToLogin() {
        UI.getCurrent().navigate(LoginView.class);
    }
}
