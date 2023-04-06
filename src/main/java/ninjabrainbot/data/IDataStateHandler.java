package ninjabrainbot.data;

import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public interface IDataStateHandler {

	IDataState getDataState();

	IModificationLock getModificationLock();

	void reset();

	void resetIfNotLocked();

	void undo();

	void undoIfNotLocked();

	void removeThrow(IThrow t);

	void resetDivineContext();

	void changeLastAngleIfNotLocked(boolean positive, NinjabrainBotPreferences preferences);

	void toggleAltStdOnLastThrowIfNotLocked();

	public ISubscribable<IDataState> whenDataStateModified();

	public void addThrowStream(ISubscribable<IThrow> stream);

	public void addFossilStream(ISubscribable<Fossil> stream);

}
