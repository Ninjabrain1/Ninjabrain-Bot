package ninjabrainbot.model.input;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.ILimitedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPositionInputSource;

/**
 * Listens to a stream of player position inputs and decides if/how the inputs should affect the data state.
 */
public class PlayerPositionInputHandler implements IDisposable {

	private final IActionExecutor actionExecutor;
	private final IInputtedPlayerPositionToActionMapper inputtedPlayerPositionToActionMapper;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public PlayerPositionInputHandler(IPlayerPositionInputSource playerPositionInputSource, IActionExecutor actionExecutor, IInputtedPlayerPositionToActionMapper inputtedPlayerPositionToActionMapper) {
		this.actionExecutor = actionExecutor;
		this.inputtedPlayerPositionToActionMapper = inputtedPlayerPositionToActionMapper;
		disposeHandler.add(playerPositionInputSource.whenNewDetailedPlayerPositionInputted().subscribe(this::onNewDetailedPlayerPositionInputted));
		disposeHandler.add(playerPositionInputSource.whenNewLimitedPlayerPositionInputted().subscribe(this::onNewLimitedPlayerPositionInputted));
	}

	private void onNewDetailedPlayerPositionInputted(IDetailedPlayerPosition detailedPlayerPosition) {
		actionExecutor.executeImmediately(inputtedPlayerPositionToActionMapper.getActionsForInputtedPlayerPosition(detailedPlayerPosition));
	}

	private void onNewLimitedPlayerPositionInputted(ILimitedPlayerPosition playerPosition) {
		actionExecutor.executeImmediately(inputtedPlayerPositionToActionMapper.getActionsForInputtedLimitedPlayerPosition(playerPosition));
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
