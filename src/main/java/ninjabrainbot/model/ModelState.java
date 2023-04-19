package ninjabrainbot.model;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.ActionExecutor;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.DataState;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.DomainModel;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.environmentstate.EnvironmentState;
import ninjabrainbot.model.environmentstate.IEnvironmentState;

public class ModelState implements IDisposable {

	public final IDomainModel domainModel;
	public final IActionExecutor actionExecutor;
	public final IEnvironmentState environmentState;
	public final IDataState dataState;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public ModelState(NinjabrainBotPreferences preferences) {
		DomainModel domainModel = disposeHandler.add(new DomainModel());
		this.domainModel = domainModel;
		actionExecutor = new ActionExecutor(domainModel);
		environmentState = disposeHandler.add(new EnvironmentState(domainModel, preferences));
		dataState = disposeHandler.add(new DataState(domainModel, environmentState));
		domainModel.finishInitialization();
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
