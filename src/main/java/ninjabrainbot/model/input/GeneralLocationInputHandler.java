package ninjabrainbot.model.input;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.common.SetGeneralLocationAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.StructurePosition;

/**
 * Listens to the stream of fossils and decides if/how the fossils should be inputted into the data state.
 */
public class GeneralLocationInputHandler implements IDisposable {

	private final IDataState dataState;
	private final IActionExecutor actionExecutor;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public GeneralLocationInputHandler(IGeneralLocationInputSource generalLocationInputSource, IDataState dataState, IActionExecutor actionExecutor) {
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;
		disposeHandler.add(generalLocationInputSource.whenNewGeneralLocationInputted().subscribe(this::onNewGeneralLocation));
	}

	private void onNewGeneralLocation(StructurePosition pos) {
		if (dataState.locked().get())
			return;

		actionExecutor.executeImmediately(new SetGeneralLocationAction(dataState.allAdvancementsDataState(), pos));
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
