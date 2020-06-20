package at.fhj.swd.travx.util;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public final class InputUtils {
    public static String getText(TextInputLayout input) {
        return Objects.requireNonNull(Objects.requireNonNull(input.getEditText()).getText()).toString();
    }

    public static Long getLong(TextInputLayout input) {
        String number = getText(input);
        return Long.valueOf(number.isEmpty() ? "0" : number);
    }
}
