package ninjabrainbot.calculator;

public class DataStateHandler implements IDataStateHandler {

	private IDataState dataState;

	public DataStateHandler(IDataState dataState) {
		this.dataState = dataState;
	}
	
	public void reset() {
		dataState.reset();
	}
	
	public void resetIfNotLocked() {
		if (!dataState.locked().get())
			reset();
	}
	
	public void undo() {
		// TODO
	}

	public void undoIfNotLocked() {
		if (!dataState.locked().get())
			undo();
	}

	public void changeLastAngleIfNotLocked(double delta) {
		if (!dataState.locked().get() && dataState.getThrowSet().size() != 0) {
			IThrow last = dataState.getThrowSet().getLast();
			if (last != null)
				last.addCorrection(delta);
		}
	}

	public void toggleAltStdOnLastThrowIfNotLocked() {
		if (!dataState.locked().get() && dataState.getThrowSet().size() != 0) {
			IThrow last = dataState.getThrowSet().getLast();
			int stdProfile = last.getStdProfileNumber();
			switch (stdProfile) {
			case StandardStdProfile.NORMAL:
				last.setStdProfileNumber(StandardStdProfile.ALTERNATIVE);
				break;
			case StandardStdProfile.ALTERNATIVE:
				last.setStdProfileNumber(StandardStdProfile.NORMAL);
				break;
			case StandardStdProfile.MANUAL:
				break;
			}
		}
	}

}
