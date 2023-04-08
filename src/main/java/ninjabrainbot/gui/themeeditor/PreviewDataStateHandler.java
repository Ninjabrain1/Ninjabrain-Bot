package ninjabrainbot.gui.themeeditor;

import java.util.List;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class PreviewDataStateHandler implements IDataStateHandler {

	private final ObservableProperty<IDataState> whenDataStateModified = new ObservableProperty<IDataState>();
	private final IModificationLock modificationLock = new AlwaysUnlocked();

	private final PreviewDataState dataState;

	public PreviewDataStateHandler(ICalculatorResult result, List<IThrow> eyeThrows, Fossil fossil, boolean locked) {
		dataState = new PreviewDataState(result, eyeThrows, fossil);
		if (locked)
			dataState.locked().set(true);
	}

	@Override
	public IDataState getDataState() {
		return dataState;
	}

	@Override
	public void changeLastAngleIfNotLocked(boolean positive, NinjabrainBotPreferences preferences) {
	}

	@Override
	public void toggleAltStdOnLastThrowIfNotLocked() {
	}

	@Override
	public ISubscribable<IDataState> whenDataStateModified() {
		return whenDataStateModified;
	}

}
