package com.how2java;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author:Created by wx on 2019/6/19
 * Desc:
 */
public class DateFormat {
    private static final String PATTERN_ONE = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_ONE);

    public static String formateDate(Date date){
        if (date == null){
            return null;
        }

        return simpleDateFormat.format(date);
    }

    public static Date parseDateStr(String data){
        if (data == null){
            return null;
        }

        try {
            return simpleDateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
