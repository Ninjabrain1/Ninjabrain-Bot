package ninjabrainbot.integrationtests;

import ninjabrainbot.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

public class ApplicationTests {

	@Disabled("Disabled until Theme.loadThemes() is made an instance method.")
	@Test
	void doesNotCrashOnStartup() {
		try {
			Main.main(new String[0]);
		} catch (Exception e) {
			Assertions.fail(e);
		}
	}
}
