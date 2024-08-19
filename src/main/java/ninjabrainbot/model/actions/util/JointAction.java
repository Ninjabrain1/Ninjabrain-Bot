package ninjabrainbot.model.actions.util;

import ninjabrainbot.model.actions.IAction;

public class JointAction implements IAction {

	private final IAction action1;
	private final IAction action2;

	public JointAction(IAction action1, IAction action2) {
		this.action1 = action1;
		this.action2 = action2;
	}

	@Override
	public void execute() {
		action1.execute();
		action2.execute();
	}
}
