package ninjabrainbot.integrationtests;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ninjabrainbot.io.LanguageResources;
import ninjabrainbot.util.I18n;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LanguageIntegrationTests {

	private static final Pattern FORMAT_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[dfs]");

	@Test
	void allTranslationsAreFormattedCorrectly() {
		Locale baseLocale = LanguageResources.getDefaultLocale();
		List<Locale> locales = LanguageResources.getSupportedLocales();

		ResourceBundle base = ResourceBundle.getBundle("lang/I18n", baseLocale, new I18n.UTF8Control());

		for (Locale locale : locales) {
			System.out.println("Checking locale: " + locale);
			ResourceBundle bundle = ResourceBundle.getBundle("lang/I18n", locale, new I18n.UTF8Control());

			validateKeys(base, bundle);
			validateFormats(base, bundle);
		}
	}

	private static void validateKeys(ResourceBundle base, ResourceBundle bundle) {
		for (String key : base.keySet()) {
			if (!bundle.containsKey(key)) {
				System.out.println("Missing key: " + key);
			}
		}
	}

	private static void validateFormats(ResourceBundle base, ResourceBundle bundle) {
		for (String key : base.keySet()) {

			String baseValue = base.getString(key);
			String otherValue = bundle.getString(key);

			List<String> baseFormats = extractFormats(baseValue);
			List<String> otherFormats = extractFormats(otherValue);

			if (!baseFormats.equals(otherFormats)) {
				Assertions.fail("Format mismatch in key: " + key + ". Base:  " + baseFormats + ". Other: " + otherFormats);
			}
		}
	}

	private static List<String> extractFormats(String text) {
		Matcher matcher = FORMAT_PATTERN.matcher(text);
		List<String> formats = new ArrayList<>();
		while (matcher.find()) {
			formats.add(matcher.group());
		}
		return formats;
	}

}
