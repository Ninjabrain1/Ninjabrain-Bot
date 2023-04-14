package ninjabrainbot.io;

import java.util.function.Consumer;

public interface IUpdateChecker {

	void check(Consumer<VersionURL> downloadUrlCallback);

}
