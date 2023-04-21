package ninjabrainbot.io;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ninjabrainbot.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LanguageResourcesTest {

	@Test
	void allLanguagesAreRegistered() {
		URL url = Main.class.getResource("/lang");
		String path = Objects.requireNonNull(url).getPath();
		File[] languageFiles = new File(path).listFiles();
		List<String> languageFileNames = Arrays.stream(Objects.requireNonNull(languageFiles)).map(File::getName).collect(Collectors.toList());
		Assertions.assertIterableEquals(languageFileNames, LanguageResources.getAllLanguageResourceNames());
	}

}