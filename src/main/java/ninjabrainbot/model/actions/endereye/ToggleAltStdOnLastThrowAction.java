package ninjabrainbot.model.actions.endereye;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.domainmodel.IListComponent;

public class ToggleAltStdOnLastThrowAction implements IAction {

	private final IDataState dataState;
	private final NinjabrainBotPreferences preferences;

	public ToggleAltStdOnLastThrowAction(IDataState dataState, NinjabrainBotPreferences preferences) {
		this.dataState = dataState;
		this.preferences = preferences;
	}

	@Override
	public void execute() {
		if (dataState.locked().get() || dataState.getThrowList().size() == 0)
			return;

		if (!preferences.useAltStd.get())
			return;

		IListComponent<IEnderEyeThrow> throwList = dataState.getThrowList();
		IEnderEyeThrow lastThrow = throwList.get(throwList.size() - 1);
		IEnderEyeThrow newThrow = lastThrow.withToggledAltStd();
		if (lastThrow == newThrow)
			return;

		throwList.replace(lastThrow, newThrow);
	}

}
