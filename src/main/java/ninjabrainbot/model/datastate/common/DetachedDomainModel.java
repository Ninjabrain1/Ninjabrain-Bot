package ninjabrainbot.model.datastate.common;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.domainmodel.IFundamentalComponent;
import ninjabrainbot.model.domainmodel.IInferredComponent;

public class DetachedDomainModel implements IDomainModel {

	ISubscribable<IDomainModel> whenModified = new ObservableProperty<>();

	@Override
	public void registerFundamentalComponent(IFundamentalComponent<?, ?> fundamentalComponent) {
	}

	@Override
	public void registerInferredComponent(IInferredComponent<?> inferredComponent) {
	}

	@Override
	public <T> ISubscribable<T> createExternalEventFor(ISubscribable<T> subscribable) {
		return subscribable;
	}

	@Override
	public void checkWriteAccess() {
	}

	@Override
	public void acquireWriteLock() {
	}

	@Override
	public void releaseWriteLock() {
	}

	@Override
	public void reset() {
	}

	@Override
	public void undoUnderWriteLock() {
	}

	@Override
	public void redoUnderWriteLock() {
	}

	@Override
	public boolean isReset() {
		return true;
	}

	@Override
	public boolean isExternalSubscriptionRegistrationAllowed() {
		return true;
	}

	@Override
	public boolean isInternalSubscriptionRegistrationAllowed() {
		return true;
	}

	@Override
	public ISubscribable<IDomainModel> whenModified() {
		return whenModified;
	}

	@Override
	public Runnable applyWriteLock(Runnable runnable) {
		return runnable;
	}
}
