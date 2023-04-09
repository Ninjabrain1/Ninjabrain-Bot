package ninjabrainbot.data.input;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.actions.IActionExecutor;
import ninjabrainbot.data.actions.SetFossilAction;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;

/**
 * Listens to the stream of fossils and decides if/how the fossils should be inputted into the data state.
 */
public class FossilInputHandler implements IDisposable {

	private final IDataState dataState;
	private final IActionExecutor actionExecutor;

	DisposeHandler disposeHandler = new DisposeHandler();

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
