package org.maktab.criminalintent.utils;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    public static final int YEAR_START = 2000;
    public static final int YEAR_END = 2020;

    public static Date randomDate() {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(YEAR_START, YEAR_END);
        gc.set(gc.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        return gc.getTime();
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}
