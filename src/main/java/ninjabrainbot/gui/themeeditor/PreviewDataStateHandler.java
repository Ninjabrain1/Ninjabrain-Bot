package ninjabrainbot.gui.themeeditor;

import java.util.List;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class PreviewDataStateHandler implements IDataStateHandler {

	private final ObservableProperty<IDataState> whenDataStateModified = new ObservableProperty<>();

	private final PreviewDataState dataState;

	public PreviewDataStateHandler(ICalculatorResult result, List<IEnderEyeThrow> eyeThrows, Fossil fossil, boolean locked) {
		dataState = new PreviewDataState(result, eyeThrows, fossil);
		if (locked)
			dataState.locked().set(true);
	}

	@Override
	public IDataState getDataState() {
		return dataState;
	}

	@Override
	public void toggleAltStdOnLastThrowIfNotLocked() {
	}

	@Override
	public ISubscribable<IDataState> whenDataStateModified() {
		return whenDataStateModified;
	}

}
