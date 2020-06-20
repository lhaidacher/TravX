package at.fhj.swd.travx.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    public static String format(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY).format(date);
    }
}
