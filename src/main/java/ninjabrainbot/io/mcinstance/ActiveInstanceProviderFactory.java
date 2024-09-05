package ninjabrainbot.io.mcinstance;

import java.io.IOException;

import com.sun.jna.Platform;
import ninjabrainbot.util.Logger;

public abstract class ActiveInstanceProviderFactory {

	public static IActiveInstanceProvider createPlatformSpecificActiveInstanceProvider() {
		try {
			if (Platform.getOSType() == Platform.WINDOWS) {
				return new WindowsActiveInstanceListener();
			}
			return new UnsupportedOSActiveInstanceProvider();
		} catch (IOException exception) {
			Logger.log("Cannot monitor active Minecraft instance.");
			Logger.log(exception);
		}
		return new UnsupportedOSActiveInstanceProvider();
	}

}
