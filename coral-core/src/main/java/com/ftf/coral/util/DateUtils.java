package com.ftf.coral.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getSimpleDateString(Date date) {
        if (date == null) {
            return "";
        }
        return SIMPLE_DATE_FORMAT.format(date);
    }
}
