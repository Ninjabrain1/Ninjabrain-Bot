package ninjabrainbot.data.information;

import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.mcinstance.MinecraftInstance;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class McVersionWarningProvider extends InformationMessageProvider {

	private final NinjabrainBotPreferences preferences;

	public McVersionWarningProvider(IActiveInstanceProvider activeInstanceListener, NinjabrainBotPreferences preferences) {
		this.preferences = preferences;
		sh.add(activeInstanceListener.activeMinecraftInstance().subscribe(this::updateInformationMessage));
		sh.add(preferences.mcVersion.whenModified().subscribe(__ -> updateInformationMessage(activeInstanceListener.activeMinecraftInstance().get())));
	}

	private void updateInformationMessage(MinecraftInstance minecraftInstance) {
		InformationMessage informationMessageToShow = minecraftInstance == null || minecraftInstance.getMcVersion() == preferences.mcVersion.get() ? null : geOrCreateWarningMessage();
		setInformationMessage(informationMessageToShow);
	}

	private InformationMessage warningMessage = null;

	private InformationMessage geOrCreateWarningMessage() {
		if (warningMessage == null)
			warningMessage = new InformationMessage(InformationType.Warning, I18n.get("information.wrong_mc_version"));
		return warningMessage;
	}

}
