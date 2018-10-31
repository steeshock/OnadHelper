package ru.steeshock.protocols.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String format(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'в' HH:mm:ss z", Locale.getDefault());

        return sdf.format(date);
    }
}
