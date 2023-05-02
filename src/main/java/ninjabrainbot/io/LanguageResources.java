package ninjabrainbot.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
		for (String fileName : getAllLanguageResourceNames()) {
			locales.add(getLocale(fileName));
		}
		return locales;
	}

	public static List<String> getAllLanguageResourceNames() {
		// Could not find an easy solution to get all language resource files,
		// it seems trivial but isn't for some reason. All solutions either work
		// in dev environment, or when packaged as jar, but never both.
		// For now, they are just hard coded, with a test to make sure that
		// this function returns all files in the directory.
		ArrayList<String> list = new ArrayList<>();
		list.add("I18n_en_US.properties");
		list.add("I18n_es_ES.properties");
		list.add("I18n_it_IT.properties");
		list.add("I18n_ja_JP.properties");
		list.add("I18n_ja_Ryukyuan.properties");
		list.add("I18n_ko_KR.properties");
		list.add("I18n_pt_BR.properties");
		list.add("I18n_ru_RU.properties");
		list.add("I18n_zh_CN.properties");
		list.add("I18n_zh_TW.properties");
		list.add("I18n_fr_FR.properties");
		list.add("I18n_cs_CS.properties");
		list.add("I18n_tr_TR.properties");
		return list;
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
