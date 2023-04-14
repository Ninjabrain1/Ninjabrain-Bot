package ninjabrainbot.util;

import java.util.function.Consumer;

import ninjabrainbot.io.IUpdateChecker;
import ninjabrainbot.io.VersionURL;

public class FakeUpdateChecker implements IUpdateChecker {

	@Override
	public void check(Consumer<VersionURL> downloadUrlCallback) {

	}

}
