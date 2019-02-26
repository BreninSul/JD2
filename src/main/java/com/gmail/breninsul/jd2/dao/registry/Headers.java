package com.gmail.breninsul.jd2.dao.registry;


import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

@Log4j2
public class Headers {
        public static final String REG_PROPS_FILENAME ="registry";
    private static String setBundle(String target) {
        String result = "";
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(REG_PROPS_FILENAME);
            result = resourceBundle.getString(target);
        } catch (Exception e) {
            log.error("There is some problem getting info aboth registrys property from file",e);
        }
        return result;
    }

    public static String get(String s) {
        return setBundle(s);
    }
}
