package at.fhj.swd.travx.util;

import java.text.DecimalFormat;
import java.util.Locale;

public final class StringUtils {
    public static String format(Float num) {
        return new DecimalFormat("0.##").format(num);
    }
}
