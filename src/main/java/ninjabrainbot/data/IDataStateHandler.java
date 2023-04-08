package ninjabrainbot.data;

import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public interface IDataStateHandler {

	IDataState getDataState();

	void changeLastAngleIfNotLocked(boolean positive, NinjabrainBotPreferences preferences);

	void toggleAltStdOnLastThrowIfNotLocked();

	ISubscribable<IDataState> whenDataStateModified();

}
