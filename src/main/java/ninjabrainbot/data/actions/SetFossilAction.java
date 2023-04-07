package ninjabrainbot.data.actions;

import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.divine.IDivineContext;

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
