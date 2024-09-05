package ninjabrainbot.integrationtests;

import ninjabrainbot.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
