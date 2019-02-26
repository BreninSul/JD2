package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.service.UserService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Slf4j
public abstract class DivWithLoginFunction extends Div {

    public static final int DURATION = 10000;
    @Autowired
    private DaoAuthenticationProvider authenticationProvider;
    protected boolean performLogin(String name, String pass) {
        Authentication auth = new UsernamePasswordAuthenticationToken(name,
                pass);
        log.info("trying to authentificate user " + name);
        try {
            final Authentication authenticated = authenticationProvider.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            log.info("Log in user=" + authenticated.getCredentials() + " sucsesfull:" + authenticated.isAuthenticated());
            return true;
        } catch (BadCredentialsException | NullPointerException e) {
            Notification.show(getTranslation("login.wron.usernameorpass.notification"), DURATION, Notification.Position.MIDDLE);
            return false;

        } catch (DisabledException e){
            Notification.show(getTranslation("login.wron.notactivated.notification"), DURATION, Notification.Position.MIDDLE);
            return false;
        } catch (Exception e){
            log.warn(e.getMessage());
            return false;
        }
    }
}
