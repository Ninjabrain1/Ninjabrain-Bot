package ninjabrainbot.util;

import ninjabrainbot.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.prefs.Preferences;

/**
 * @author LingMuQingYu
 * @since 2021/12/25 14:39
 */
public class I18n {

    private static final List<Locale> LANGUAGE_CONFIG = new ArrayList<>();

    private static ResourceBundle BUNDLE = null;

    private static Locale LANGUAGE;

    private static final List<String> LANGUAGE_NAMES = new ArrayList<>();

    static {
        LANGUAGE_CONFIG.add(Locale.US);
        LANGUAGE_CONFIG.add(Locale.KOREA);
        LANGUAGE_CONFIG.add(Locale.SIMPLIFIED_CHINESE);
        LANGUAGE_CONFIG.add(Locale.ITALY);
        final Preferences preferences = Preferences.userNodeForPackage(Main.class);
        LANGUAGE = Locale.getDefault();
        final Integer language = preferences.getInt("language", -1);

        int i = 0;
        for (Locale value : LANGUAGE_CONFIG) {
            if (i == language || (language == -1 && value.equals(LANGUAGE))) {
                LANGUAGE = value;
                BUNDLE = ResourceBundle.getBundle("resources/lang/I18n", value, new UTF8Control());
            }
            i++;
        }
        if (Objects.isNull(BUNDLE)) {
            LANGUAGE = Locale.US;
            BUNDLE = ResourceBundle.getBundle("resources/lang/I18n", LANGUAGE, new UTF8Control());
        }
        for (Locale value : LANGUAGE_CONFIG) {
            LANGUAGE_NAMES.add(BUNDLE.getString("settings.language." + value));
        }
        System.out.println(LANGUAGE_NAMES);
    }

    public static String getDefaultName() {
        return LANGUAGE_NAMES.get(LANGUAGE_CONFIG.indexOf(LANGUAGE));
    }

    public static List<String> getLanguageNames() {
        return LANGUAGE_NAMES;
    }

    public static String get(String key, Object... args) {
        return String.format(BUNDLE.getString(key), args);
    }

    public static class UTF8Control extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
            // The below is a copy of the default implementation.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // Only this line is changed to make it to read properties files as UTF-8.
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
