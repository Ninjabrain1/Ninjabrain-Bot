package ninjabrainbot.data.information;

import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.SubscriptionHandler;

public class InformationMessageProvider extends ObservableField<InformationMessage> implements IDisposable {

	SubscriptionHandler sh = new SubscriptionHandler();

	protected void setInformationMessage(InformationMessage informationMessage) {
		set(informationMessage);
	}

	@Override
	public void dispose() {
		sh.dispose();
	}

}
