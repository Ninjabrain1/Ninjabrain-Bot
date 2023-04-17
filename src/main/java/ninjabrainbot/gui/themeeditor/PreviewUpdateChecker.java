package ninjabrainbot.gui.themeeditor;

import java.util.function.Consumer;

import ninjabrainbot.io.updatechecker.IUpdateChecker;
import ninjabrainbot.io.updatechecker.VersionURL;

public class PreviewUpdateChecker implements IUpdateChecker {
	@Override
	public void check(Consumer<VersionURL> downloadUrlCallback) {
	}
}
