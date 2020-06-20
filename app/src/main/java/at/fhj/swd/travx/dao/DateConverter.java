package at.fhj.swd.travx.dao;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);

    @TypeConverter
    public static Date fromTimestamp(String value) {
        Date date = null;

        try {
            date = df.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    @TypeConverter
    public static String dateToTimestamp(Date value) {
        return value == null ? null : df.format(value);
    }
}
