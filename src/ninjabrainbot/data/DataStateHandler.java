package ninjabrainbot.data;

import ninjabrainbot.data.blind.BlindPosition;
import ninjabrainbot.data.calculator.Calculator;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.endereye.IStdProfile;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.StandardStdProfile;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.event.SubscriptionHandler;

public class DataStateHandler implements IDataStateHandler, IDisposable, IModificationLock {

	private final IStdProfile stdProfile;

	private DataState dataState;

	private ObservableProperty<IDataState> whenDataStateModified = new ObservableProperty<IDataState>();

	private SubscriptionHandler sh = new SubscriptionHandler();

	public DataStateHandler(IStdProfile stdProfile) {
		this.stdProfile = stdProfile;
		dataState = new DataState(new Calculator());
	}

	@Override
	public IDataState getDataState() {
		return dataState;
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

	@Override
	public void addThrowStream(ISubscribable<IThrow> stream) {
		stream.subscribe(t -> onNewThrow(t));
	}

	@Override
	public void addFossilStream(ISubscribable<Fossil> stream) {
		stream.subscribe(f -> dataState.setFossil(f));
	}

	@Override
	public ISubscribable<IDataState> whenDataStateModified() {
		return whenDataStateModified;
	}
	
	@Override
	public IModificationLock getModificationLock() {
		return this;
	}
	
	@Override
	public boolean isLocked() {
		return false;
	}

	private void onNewThrow(IThrow t) {
		dataState.setPlayerPos(t);
		if (dataState.locked().get())
			return;
		if (t.isNether()) {
			if (dataState.getThrowSet().size() == 0)
				dataState.setBlindPosition(new BlindPosition(t));
			return;
		}
		if (!t.lookingBelowHorizon()) {
			t.setStdProfile(stdProfile);
			dataState.getThrowSet().add(t);
		}
	}
	
	@Override
	public void dispose() {
		sh.dispose();
	}

}
