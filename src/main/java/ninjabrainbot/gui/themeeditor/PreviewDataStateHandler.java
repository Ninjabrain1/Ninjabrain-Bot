package ninjabrainbot.gui.themeeditor;

import java.util.List;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.IDataStateHandler;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;

public class PreviewDataStateHandler implements IDataStateHandler {

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

}
