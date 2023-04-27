package ninjabrainbot.util;

import java.util.function.Consumer;

import ninjabrainbot.io.updatechecker.IUpdateChecker;
import ninjabrainbot.io.updatechecker.VersionURL;

public class FakeUpdateChecker implements IUpdateChecker {

	@Override
	public void check(Consumer<VersionURL> downloadUrlCallback) {

	}

}
