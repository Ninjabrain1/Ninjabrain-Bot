package ninjabrainbot.event;

import java.util.ArrayList;

public class SubscriptionHandler implements IDisposable {

	public ArrayList<Subscription> subscriptions = new ArrayList<Subscription>(2);

	public void add(Subscription s) {
		subscriptions.add(s);
	}

	@Override
	public void dispose() {
		for (Subscription s : subscriptions) {
			s.cancel();
		}
	}

}
