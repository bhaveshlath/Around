package com.example.blath.around.commons.Utils;

import com.example.blath.around.models.DateRange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by blath on 8/10/17.
 */

public class DateUtils {

    public static DateRange getDateRange(String startDateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        DateRange dateRange = null;
        try{
            Date startDate = simpleDateFormat.parse(startDateString);
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            c.add(Calendar.DATE, 30);
            dateRange = new DateRange(startDate, c.getTime());
        }catch(ParseException e){
            e.getStackTrace();
        }
        return dateRange;
    }

    public static String dateFormatterFromString(String dateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");
        return simpleDateFormat.format(new Date(dateString));
    }

    public static String dateFormatter(int monthOfYear, int dayOfMonth, int year){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        return simpleDateFormat.format(new Date((monthOfYear + 1) + "/" + dayOfMonth + "/" + year));
    }

    public static String timeFormatter(String timeString){
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            return _12HourSDF.format(_24HourSDF.parse(timeString));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String twoDigitDayOfMonth(Calendar calendar){
        SimpleDateFormat month_date = new SimpleDateFormat("dd");
        return month_date.format(calendar.getTime());
    }

    public static String monthName(Calendar calendar){
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        return month_date.format(calendar.getTime());
    }

    public static String weekDayName(Calendar calendar){
        SimpleDateFormat month_date = new SimpleDateFormat("EE");
        return month_date.format(calendar.getTime());
    }

    public static String lastTwoDigitYear(Calendar calendar){
        SimpleDateFormat month_date = new SimpleDateFormat("yy");
        return month_date.format(calendar.getTime());
    }
}
