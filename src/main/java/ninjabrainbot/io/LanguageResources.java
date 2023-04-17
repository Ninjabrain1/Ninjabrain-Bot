package ninjabrainbot.io;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ninjabrainbot.Main;
import ninjabrainbot.util.Assert;

public class LanguageResources {

	private static final List<Locale> supportedLocales = readSupportedLocales();

	public static List<Locale> getSupportedLocales() {
		return supportedLocales;
	}

	public static Locale getDefaultLocale() {
		return Locale.US;
	}

	public static Locale getLocaleFromTag(String tag) {
		for (Locale locale : supportedLocales) {
			if (locale.toLanguageTag().equals(tag)) {
				return locale;
			}
		}
		return null;
	}

	public static boolean isLocaleSupported(Locale locale) {
		for (Locale supportedLocale : supportedLocales) {
			if (supportedLocale.toLanguageTag().equals(locale.toLanguageTag())) {
				return true;
			}
		}
		return false;
	}

	private static List<Locale> readSupportedLocales() {
		ArrayList<Locale> locales = new ArrayList<>();
		for (File file : getLanguageResourceFiles()) {
			Assert.isTrue(file.isFile(), "Expected resources/lang to only have files.");
			locales.add(getLocale(file.getName()));
		}
		return locales;
	}

	private static File[] getLanguageResourceFiles() {
		URL url = Main.class.getResource("/lang");
		String path = Objects.requireNonNull(url).getPath();
		return new File(path).listFiles();
	}

	private static Locale getLocale(String resourceFileName) {
		Assert.isTrue(resourceFileName.startsWith("I18n"));
		Assert.isTrue(resourceFileName.endsWith(".properties"));

		String truncatedName = resourceFileName.substring(4, resourceFileName.length() - 11);
		if (truncatedName.isEmpty())
			return getDefaultLocale();

		Assert.isTrue(truncatedName.startsWith("_"));
		truncatedName = truncatedName.substring(1);

		String[] localeData = truncatedName.split("_");
		Assert.isEqual(localeData.length, 2);
		String language = localeData[0];
		String country = localeData[1];
		return new Locale(language, country);
	}

}
