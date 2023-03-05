package ninjabrainbot.io.mcinstance;

import com.sun.jna.Platform;

public abstract class ActiveInstanceProviderFactory {

	public static IActiveInstanceProvider createPlatformSpecificActiveInstanceProvider() {
		switch (Platform.getOSType()) {
		case Platform.WINDOWS:
			return new WindowsActiveInstanceListener();
		default:
			return new UnsupportedOSActiveInstanceProvider();
		}
	}

}
