package ninjabrainbot.data.information;

import ninjabrainbot.io.ActiveInstanceListener;
import ninjabrainbot.io.MinecraftInstance;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class McVersionWarningProvider extends InformationMessageProvider {

	private NinjabrainBotPreferences preferences;

	public McVersionWarningProvider(ActiveInstanceListener activeInstanceListener, NinjabrainBotPreferences preferences) {
		this.preferences = preferences;
		sh.add(activeInstanceListener.whenActiveMinecraftInstanceChanged().subscribe(minecraftInstance -> updateInformationMessage(minecraftInstance)));
		sh.add(preferences.mcVersion.whenModified().subscribe(__ -> updateInformationMessage(activeInstanceListener.getActiveMinecraftInstance())));
	}

	private void updateInformationMessage(MinecraftInstance minecraftInstance) {
		InformationMessage informationMessageToShow = minecraftInstance == null || minecraftInstance.getMcVersion() == preferences.mcVersion.get() ? null : geOrCreatetWarningMessage();
		setInformationMessage(informationMessageToShow);
	}

	private InformationMessage warningMessage = null;

	private InformationMessage geOrCreatetWarningMessage() {
		if (warningMessage == null)
			warningMessage = new InformationMessage(InformationType.Warning, I18n.get("information.wrong_mc_version"));
		return warningMessage;
	}

}
