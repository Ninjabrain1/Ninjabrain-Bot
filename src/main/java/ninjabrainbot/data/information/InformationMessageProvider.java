package ninjabrainbot.data.information;

import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.SubscriptionHandler;
import ninjabrainbot.io.preferences.BooleanPreference;

public abstract class InformationMessageProvider extends ObservableField<InformationMessage> implements IDisposable {

	SubscriptionHandler sh = new SubscriptionHandler();

	BooleanPreference enabledPreference;

	InformationMessageProvider(){
	}

	InformationMessageProvider(BooleanPreference enabledPreference){
		this.enabledPreference = enabledPreference;
		sh.add(enabledPreference.whenModified().subscribe(__ -> raiseInformationMessageChanged()));
	}

	protected abstract boolean shouldShowInformationMessage();

	protected abstract InformationMessage getInformationMessage();

	protected void raiseInformationMessageChanged(){
		boolean disabledByPreference = enabledPreference != null && !enabledPreference.get();
		InformationMessage informationMessageToShow = shouldShowInformationMessage() && !disabledByPreference ? getInformationMessage() : null;
		set(informationMessageToShow);
	}

	@Override
	public void dispose() {
		sh.dispose();
	}

}
