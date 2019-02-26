package com.gmail.breninsul.jd2.view;

import com.vaadin.flow.component.Component;
import lombok.extern.log4j.Log4j2;
import org.jooq.Role;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Log4j2
public class ViewUtils {
    private ViewUtils() {
    }
    public  static List<String> logFile=new ArrayList<>();

    public static List<String> getLog() {
        List<String> temp=logFile;
        if (logFile.size()>20){
            for (int i = 0; i <logFile.size()-20 ; i++) {
                logFile.remove(0);
            }
        }
        log.info(temp);
        return temp;
    }

    public static void addToLog(String s) {
        logFile.add(s);
    }

    public static void hideOrUnhideComponent(Component c) {
        if (c.isVisible()) {
            c.setVisible(false);
        } else {
            c.setVisible(true);
        }
    }

    public static String getUsername() {
        String name = null;
        if (SecurityContextHolder.getContext().
                getAuthentication() != null) {
            name = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!name.equals("anonymousUser")) {
            } else {
                name = null;
            }
        }
        return name;
    }
    public static Collection getRoles() {
        String name = null;
        if (SecurityContextHolder.getContext().
                getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        }
        return null;
    }

    public static boolean isLoggined() {
        return getUsername() != null;
    }
}
