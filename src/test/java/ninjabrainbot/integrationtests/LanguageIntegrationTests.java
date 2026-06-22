package ninjabrainbot.integrationtests;

import java.util.ArrayList;
import java.util.Date;
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

	private static final Pattern FORMAT_PATTERN = Pattern.compile("%(?:([1-9]\\d*)\\$|(<))?([-#+ 0,(]*)?(\\d+)?(?:\\.(\\d+))?([tT])?([a-zA-Z%])");

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

			List<String> baseFormats = extractFormats(key, baseValue);
			List<String> otherFormats = extractFormats(key, otherValue);

			validateFormatString(key, baseValue, baseFormats);
			validateFormatString(key, otherValue, otherFormats);

			if (!baseFormats.equals(otherFormats)) {
				Assertions.fail("Format mismatch in key: " + key + ". Base:  " + baseFormats + ". Other: " + otherFormats);
			}
		}
	}

	private static List<String> extractFormats(String key, String text) {
		Matcher matcher = FORMAT_PATTERN.matcher(text);
		List<String> formats = new ArrayList<>();
		int searchStart = 0;
		while (matcher.find()) {
			int unmatchedPercent = text.indexOf('%', searchStart);
			if (unmatchedPercent >= 0 && unmatchedPercent < matcher.start()) {
				Assertions.fail("Invalid format in key: " + key + ". Text contains an unescaped %: " + text);
			}
			searchStart = matcher.end();
			if (!matcher.group().equals("%%") && !matcher.group().equals("%n")) {
				formats.add(formatSignature(matcher));
			}
		}
		if (text.indexOf('%', searchStart) >= 0) {
			Assertions.fail("Invalid format in key: " + key + ". Text contains an unescaped %: " + text);
		}
		return formats;
	}

	private static String formatSignature(Matcher matcher) {
		String precision = matcher.group(5) == null ? "" : "." + matcher.group(5);
		String dateTimePrefix = matcher.group(6) == null ? "" : matcher.group(6);
		return precision + dateTimePrefix + matcher.group(7);
	}

	private static void validateFormatString(String key, String text, List<String> formats) {
		try {
			String.format(Locale.US, text, getFormatArguments(formats));
		} catch (RuntimeException e) {
			Assertions.fail("Invalid format in key: " + key + ". Text: " + text, e);
		}
	}

	private static Object[] getFormatArguments(List<String> formats) {
		Object[] arguments = new Object[formats.size()];
		for (int i = 0; i < formats.size(); i++) {
			arguments[i] = getFormatArgument(formats.get(i));
		}
		return arguments;
	}

	private static Object getFormatArgument(String format) {
		char conversion = format.charAt(format.length() - 1);
		if (format.length() > 1 && (format.charAt(format.length() - 2) == 't' || format.charAt(format.length() - 2) == 'T')) {
			return new Date();
		}
		switch (Character.toLowerCase(conversion)) {
			case 'd':
			case 'o':
			case 'x':
				return 1;
			case 'e':
			case 'f':
			case 'g':
			case 'a':
				return 1.0;
			case 'c':
				return 'a';
			case 'b':
				return true;
			case 'h':
			case 's':
				return "text";
			default:
				return "text";
		}
	}

}
