package at.fhj.swd.travx.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    public static String format(Date date) {
        return format(new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY), date);
    }

    public static String formatExtended(Date date) {
        return format(new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY), date);
    }

    private static String format(SimpleDateFormat sdf, Date date) {
        return sdf.format(date);
    }
}
