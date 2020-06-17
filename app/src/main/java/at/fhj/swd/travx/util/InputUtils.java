package at.fhj.swd.travx.util;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import at.fhj.swd.travx.R;

public final class InputUtils {
    public static String getText(TextInputLayout input) {
        return Objects.requireNonNull(Objects.requireNonNull(input.getEditText()).getText()).toString();
    }

    public static Long getLong(TextInputLayout input) {
        return Long.valueOf(getText(input));
    }
}
