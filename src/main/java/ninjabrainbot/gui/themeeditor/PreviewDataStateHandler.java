package ninjabrainbot.gui.themeeditor;

import java.util.List;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.IDataStateHandler;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
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
	public ISubscribable<IDataState> whenDataStateModified() {
		return whenDataStateModified;
	}

}
