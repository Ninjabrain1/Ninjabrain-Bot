package ninjabrainbot.data.information;

import java.util.HashMap;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.ObservableList;

public class InformationMessageList extends ObservableList<InformationMessage> {

	final HashMap<InformationMessageProvider, InformationMessage> informationMessages;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public InformationMessageList() {
		informationMessages = new HashMap<>();
	}

	public void AddInformationMessageProvider(InformationMessageProvider informationMessageProvider) {
		whenInformationMessageChanged(informationMessageProvider);
		disposeHandler.add(informationMessageProvider.subscribe(__ -> whenInformationMessageChanged(informationMessageProvider)));
	}

	private synchronized void whenInformationMessageChanged(InformationMessageProvider provider) {
		InformationMessage message = provider.get();
		if (informationMessages.containsKey(provider)) {
			remove(informationMessages.get(provider));
			informationMessages.remove(provider);
		}
		if (message != null) {
			add(message);
			informationMessages.put(provider, message);
		}
	}

}
