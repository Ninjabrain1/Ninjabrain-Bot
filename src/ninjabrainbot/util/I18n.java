package ninjabrainbot.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author zhengjin.liu
 * @since 2021/12/25 14:39
 */
public class I18n {
    private static boolean isDev = false;
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("resources/lang/I18n", Locale.getDefault());
    public static String get(String key, Object... args) {
        String value = BUNDLE.getString(key);
        if (isDev) {
            value = new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }
        return String.format(value, args);
    }

    public static void setDev(boolean dev) {
        isDev = dev;
    }
}
