package ninjabrainbot.gui.settings.themeeditor;

import java.util.List;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.divine.BuriedTreasure;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class PreviewDataStateHandler implements IDataStateHandler {

	private ObservableProperty<IDataState> whenDataStateModified = new ObservableProperty<IDataState>();
	private IModificationLock modificationLock = new AlwaysUnlocked();

	private PreviewDataState dataState;

	public PreviewDataStateHandler(ICalculatorResult result, List<IThrow> eyeThrows, Fossil f, boolean locked) {
		dataState = new PreviewDataState(result, eyeThrows, f);
		if (locked)
			dataState.toggleLocked();
	}

	@Override
	public IDataState getDataState() {
		return dataState;
	}

	@Override
	public IModificationLock getModificationLock() {
		return modificationLock;
	}

	@Override
	public void reset() {
	}

	@Override
	public void resetIfNotLocked() {
	}

	@Override
	public void undo() {
	}

	@Override
	public void undoIfNotLocked() {
	}

	@Override
	public void removeThrow(IThrow t) {
	}

	@Override
	public void resetDivineContext() {
	}

	@Override
	public void changeLastAngleIfNotLocked(double delta) {
	}

	@Override
	public void toggleAltStdOnLastThrowIfNotLocked() {
	}

	@Override
	public ISubscribable<IDataState> whenDataStateModified() {
		return whenDataStateModified;
	}

	@Override
	public void addThrowStream(ISubscribable<IThrow> stream) {
	}

	@Override
	public void addBuriedTreasureStream(ISubscribable<BuriedTreasure> stream) {
	}

	@Override
	public void addFossilStream(ISubscribable<Fossil> stream) {
	}

}
