package com.gmail.breninsul.jd2.managers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToSimpleDate {
    private static SimpleDateFormat format = new SimpleDateFormat();
    static {
        format.applyPattern("dd.MM.yyyy");
    }

    public static String get(Date date){
        return format.format(date);
    }
}
