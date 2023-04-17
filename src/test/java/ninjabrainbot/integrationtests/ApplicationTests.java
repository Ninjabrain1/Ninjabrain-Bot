package ninjabrainbot.integrationtests;

import ninjabrainbot.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApplicationTests {

	@Test
	void doesNotCrashOnStartup() {
		try {
			Main.main(new String[0]);
		} catch (Exception e) {
			Assertions.fail(e);
		}
	}
}
