package ninjabrainbot.data.information;

import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.SubscriptionHandler;

public abstract class InformationMessageProvider extends ObservableField<InformationMessage> implements IDisposable {

	SubscriptionHandler sh = new SubscriptionHandler();

	protected abstract boolean shouldShowInformationMessage();

	protected abstract InformationMessage getInformationMessage();

	protected void raiseInformationMessageChanged(){
		InformationMessage informationMessageToShow = shouldShowInformationMessage() ? getInformationMessage() : null;
		set(informationMessageToShow);
	}

	@Override
	public void dispose() {
		sh.dispose();
	}

}
