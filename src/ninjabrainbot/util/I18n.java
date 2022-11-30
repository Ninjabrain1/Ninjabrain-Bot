package ninjabrainbot.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import ninjabrainbot.Main;

/**
 * @author LingMuQingYu
 * @since 2021/12/25 14:39
 */
public class I18n {

	// add language ja_RYU
	public static final Locale ja_RYU = new Locale("ja", "RYU");
	public static final Locale ru_RU = new Locale("ru", "RU");
	
	private static final List<Locale> LANGUAGE_CONFIG = new ArrayList<>();

	private static ResourceBundle BUNDLE = null;

	private static Locale LANGUAGE;

	private static final List<String> LANGUAGE_NAMES = new ArrayList<>();

	private static ResourceBundle DEFAULT_BUNDLE = ResourceBundle.getBundle("resources/lang/I18n", Locale.US, new UTF8Control());;
	static {
		LANGUAGE_CONFIG.add(Locale.US);
		LANGUAGE_CONFIG.add(Locale.KOREA);
		LANGUAGE_CONFIG.add(Locale.SIMPLIFIED_CHINESE);
		LANGUAGE_CONFIG.add(Locale.ITALY);
		LANGUAGE_CONFIG.add(Locale.JAPAN);
		LANGUAGE_CONFIG.add(Locale.TRADITIONAL_CHINESE);
		LANGUAGE_CONFIG.add(ja_RYU);
		LANGUAGE_CONFIG.add(ru_RU);
		final Preferences preferences = Preferences.userNodeForPackage(Main.class);
		final Integer language = preferences.getInt("language", -1);
		LANGUAGE = getLanguageFromID(language);
		BUNDLE = ResourceBundle.getBundle("resources/lang/I18n", LANGUAGE, new UTF8Control());
		for (Locale value : LANGUAGE_CONFIG) {
			try{
				LANGUAGE_NAMES.add(BUNDLE.getString("settings.language." + value));
			} catch (Exception ignored){
				LANGUAGE_NAMES.add(DEFAULT_BUNDLE.getString("settings.language." + value));
			}
		}
	}

	private static Locale getLanguageFromID(int id) {
		if (id == -1) {
			Locale def = Locale.getDefault();
			for (Locale value : LANGUAGE_CONFIG) {
				if (value.equals(def)) {
					return value;
				}
			}
			return Locale.US;
		}
		return LANGUAGE_CONFIG.get(id);
	}

	public static String getDefaultName() {
		return LANGUAGE_NAMES.get(LANGUAGE_CONFIG.indexOf(LANGUAGE));
	}

	public static String[] getLanguageNames() {
		final String[] languageNames = new String[LANGUAGE_NAMES.size()];
		int i = 0;
		for (String languageName : LANGUAGE_NAMES) {
			languageNames[i] = languageName;
			i++;
		}
		return languageNames;
	}
	
	public static String getLanguageName(Locale locale) {
		return BUNDLE.getString("settings.language." + locale.toString());
	}

	public static int[] getLanguageIDs() {
		final int[] languageIds = new int[LANGUAGE_NAMES.size()];
		for (int i = 0; i < languageIds.length; i++) {
			languageIds[i] = i;
		}
		return languageIds;
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
