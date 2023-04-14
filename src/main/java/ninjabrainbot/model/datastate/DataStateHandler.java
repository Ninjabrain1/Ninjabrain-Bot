package ninjabrainbot.model.datastate;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.ActionExecutor;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.endereye.CoordinateInputSource;
import ninjabrainbot.model.datastate.endereye.EnderEyeThrowFactory;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrowFactory;
import ninjabrainbot.model.datastate.endereye.StandardDeviationHandler;
import ninjabrainbot.model.domainmodel.DomainModel;
import ninjabrainbot.model.environmentstate.EnvironmentState;
import ninjabrainbot.model.input.ActiveInstanceInputHandler;
import ninjabrainbot.model.input.ButtonInputHandler;
import ninjabrainbot.model.input.FossilInputHandler;
import ninjabrainbot.model.input.HotkeyInputHandler;
import ninjabrainbot.model.input.IButtonInputHandler;
import ninjabrainbot.model.input.PlayerPositionInputHandler;

public class DataStateHandler implements IDataStateHandler, IDisposable {

	private final DataState dataState;

	public final DomainModel domainModel;
	public final IActionExecutor actionExecutor;
	public final IButtonInputHandler buttonInputHandler;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public DataStateHandler(NinjabrainBotPreferences preferences, IClipboardProvider clipboardProvider, IActiveInstanceProvider activeInstanceProvider) {
		disposeHandler.add(domainModel = new DomainModel());
		actionExecutor = new ActionExecutor(domainModel);

		StandardDeviationHandler standardDeviationHandler = disposeHandler.add(new StandardDeviationHandler(preferences));
		EnvironmentState environmentState = disposeHandler.add(new EnvironmentState(domainModel, preferences));
		dataState = disposeHandler.add(new DataState(domainModel, environmentState));

		CoordinateInputSource coordinateInputSource = disposeHandler.add(new CoordinateInputSource(clipboardProvider));
		IEnderEyeThrowFactory enderEyeThrowFactory = new EnderEyeThrowFactory(preferences, dataState.boatDataState, standardDeviationHandler);
		disposeHandler.add(new PlayerPositionInputHandler(coordinateInputSource, dataState, actionExecutor, preferences, enderEyeThrowFactory));
		disposeHandler.add(new FossilInputHandler(coordinateInputSource, dataState, actionExecutor));
		disposeHandler.add(new ActiveInstanceInputHandler(activeInstanceProvider, domainModel, dataState, environmentState, actionExecutor, preferences));
		disposeHandler.add(new HotkeyInputHandler(preferences, domainModel, dataState, actionExecutor));

		buttonInputHandler = new ButtonInputHandler(domainModel, dataState, actionExecutor);
	}

	@Override
	public IDataState getDataState() {
		return dataState;
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
