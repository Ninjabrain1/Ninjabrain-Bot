package ninjabrainbot.model.input;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.endereye.F3IData;

/**
 * Listens to the stream of F3+I-inputs and decides if/how they should be inputted into the data state.
 */
public class F3ILocationInputHandler implements IDisposable {

	private final IActionExecutor actionExecutor;
	private final IInputtedF3IToActionMapper inputtedF3IToActionMapper;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public F3ILocationInputHandler(IF3ILocationInputSource f3iLocationInputSource, IActionExecutor actionExecutor, IInputtedF3IToActionMapper inputtedF3IToActionMapper) {
		this.inputtedF3IToActionMapper = inputtedF3IToActionMapper;
		this.actionExecutor = actionExecutor;
		disposeHandler.add(f3iLocationInputSource.whenNewF3ILocationInputted().subscribe(this::onNewF3ILocation));
	}

	private void onNewF3ILocation(F3IData f3IData) {
		actionExecutor.executeImmediately(inputtedF3IToActionMapper.getActionsForInputtedF3I(f3IData));
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
