package ninjabrainbot.data;

import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.event.ISubscribable;

public interface IDataStateHandler {

	IDataState getDataState();

	IModificationLock getModificationLock();

	void reset();

	void resetIfNotLocked();

	void undo();

	void undoIfNotLocked();

	void changeLastAngleIfNotLocked(double delta);

	void toggleAltStdOnLastThrowIfNotLocked();

	public ISubscribable<IDataState> whenDataStateModified();

	public void addThrowStream(ISubscribable<IThrow> stream);

	public void addFossilStream(ISubscribable<Fossil> stream);

}
