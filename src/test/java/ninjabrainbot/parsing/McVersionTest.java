package ninjabrainbot.parsing;

import ninjabrainbot.io.preferences.enums.McVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class McVersionTest {

	@Test
	public void versionStringsParseCorrectly() {
		Assertions.assertEquals(McVersion.PRE_119, McVersion.fromVersionString("1.13.2"));
		Assertions.assertEquals(McVersion.PRE_119, McVersion.fromVersionString("1.16"));
		Assertions.assertEquals(McVersion.PRE_119, McVersion.fromVersionString("1.16.1"));
		Assertions.assertEquals(McVersion.PRE_119, McVersion.fromVersionString("1.18.2"));

		Assertions.assertEquals(McVersion.POST_119, McVersion.fromVersionString("1.19"));
		Assertions.assertEquals(McVersion.POST_119, McVersion.fromVersionString("1.21.11"));
		Assertions.assertEquals(McVersion.POST_119, McVersion.fromVersionString("26.1"));

		Assertions.assertNull(McVersion.fromVersionString("25.1"));
	}
}
