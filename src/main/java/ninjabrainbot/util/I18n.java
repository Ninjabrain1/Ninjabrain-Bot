package ninjabrainbot.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import ninjabrainbot.Main;
import ninjabrainbot.io.LanguageResources;

/**
 * @author LingMuQingYu
 * @since 2021/12/25 14:39
 */
public class I18n {

	private static final ResourceBundle BUNDLE;

	public static final Locale LANGUAGE;

	static {
		final Preferences preferences = Preferences.userNodeForPackage(Main.class);
		final String languageTag = preferences.get("language_v2", "");
		Locale language = LanguageResources.getLocaleFromTag(languageTag);
		if (language == null) {
			Locale defaultLocale = Locale.getDefault();
			language = LanguageResources.isLocaleSupported(defaultLocale) ? defaultLocale : LanguageResources.getDefaultLocale();
			preferences.put("language_v2", language.toLanguageTag());
		}
		LANGUAGE = language;
		BUNDLE = ResourceBundle.getBundle("lang/I18n", LANGUAGE, new UTF8Control());
	}

	public static String get(String key, Object... args) {
		return String.format(BUNDLE.getString(key), args);
	}

	public static boolean localeRequiresExtraSpace() {
		return !LANGUAGE.toLanguageTag().contentEquals("en-US");
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
