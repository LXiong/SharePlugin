package com.tangzi.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liubin on 2017/10/27.
 */

public class BaseDateUtils {
    public static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String getCurrentDate() {
        return format(new Date(), DEFAULT_FORMAT);
    }

}
