package com.ali.socialblog.Util;

import java.util.Date;

public class Constant {

    public static String getTime(String time) {
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formated = dateFormat.format(new Date(Long.valueOf(time)).getTime());
        return formated;
    }
}
