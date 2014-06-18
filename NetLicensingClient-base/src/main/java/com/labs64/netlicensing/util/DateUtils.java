package com.labs64.netlicensing.util;

import java.util.Calendar;
import java.util.TimeZone;
import javax.xml.bind.DatatypeConverter;

public class DateUtils {

    public static Calendar getCurrentDate() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public static Calendar parseDate(String dateTime) {
        Calendar dateTimeCl = DatatypeConverter.parseDateTime(dateTime);
        dateTimeCl.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateTimeCl;
    }

    public static String printDate(Calendar date) {
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        return DatatypeConverter.printDateTime(date);
    }

}
