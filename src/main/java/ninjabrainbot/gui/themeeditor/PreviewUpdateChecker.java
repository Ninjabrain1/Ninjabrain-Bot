package ninjabrainbot.gui.themeeditor;

import java.util.function.Consumer;

import ninjabrainbot.io.IUpdateChecker;
import ninjabrainbot.io.VersionURL;

public class PreviewUpdateChecker implements IUpdateChecker {
	@Override
	public void check(Consumer<VersionURL> downloadUrlCallback) {
	}
}
