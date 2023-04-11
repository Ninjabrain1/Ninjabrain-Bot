package ninjabrainbot.model.actions.common;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.datastate.divine.IDivineContext;

public class SetFossilAction implements IAction {

	private final IDivineContext divineContext;
	private final Fossil fossil;

	public SetFossilAction(IDivineContext divineContext, Fossil fossil) {
		this.divineContext = divineContext;
		this.fossil = fossil;
	}

	@Override
	public void execute() {
		divineContext.fossil().set(fossil);
	}

}
