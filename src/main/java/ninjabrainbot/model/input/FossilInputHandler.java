package ninjabrainbot.model.input;

import ninjabrainbot.model.IDataState;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.common.SetFossilAction;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;

/**
 * Listens to the stream of fossils and decides if/how the fossils should be inputted into the data state.
 */
public class FossilInputHandler implements IDisposable {

	private final IDataState dataState;
	private final IActionExecutor actionExecutor;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public FossilInputHandler(IFossilInputSource fossilInputSource, IDataState dataState, IActionExecutor actionExecutor) {
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;
		disposeHandler.add(fossilInputSource.whenNewFossilInputted().subscribe(this::onNewFossil));
	}

	private void onNewFossil(Fossil fossil) {
		actionExecutor.executeImmediately(new SetFossilAction(dataState.getDivineContext(), fossil));
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
