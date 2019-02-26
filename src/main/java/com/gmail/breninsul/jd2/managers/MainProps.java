package com.gmail.breninsul.jd2.managers;

import java.util.ResourceBundle;


public class MainProps {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("main");


    public static String get(String target) {
        String result = "";
        try {
            result = resourceBundle.getString(target);
        } catch (Exception e) {
           ExeptionManager.log(e, MainProps.class);
        }
        return result;
    }

}
