package at.fhj.swd.travx.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyUtils {
    public static String format(Long l) {
        return NumberFormat.getCurrencyInstance(Locale.GERMANY).format(l);
    }

    public static String format(Double d) {
        return NumberFormat.getCurrencyInstance(Locale.GERMANY).format(d);
    }
}
