package ninjabrainbot.io.api;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.api.queries.IQuery;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.util.Assert;
import ninjabrainbot.util.Logger;

public class EventSender implements IDisposable {

	private final IDataState dataState;
	private final ExecutorService executorService;
	private final HashMap<IQuery, SubscriberList> subscribersByQuery;
	private final DisposeHandler disposeHandler = new DisposeHandler();

	public EventSender(IDataState dataState, IDomainModel domainModel, ExecutorService executorService) {
		this.dataState = dataState;
		this.executorService = executorService;
		subscribersByQuery = new HashMap<>();

		disposeHandler.add(domainModel.whenModified().subscribe(this::onDomainModelUpdated));
	}

	public synchronized void addSubscriber(IQuery query, OutputStream outputStream) {
		if (!subscribersByQuery.containsKey(query)) {
			SubscriberList subscriberList = new SubscriberList();
			subscriberList.lastSentEvent = query.get(dataState);
			subscribersByQuery.put(query, subscriberList);
		}

		SubscriberList subscriberList = subscribersByQuery.get(query);
		Assert.isFalse(subscriberList.subscribers.contains(outputStream));
		subscriberList.subscribers.add(outputStream);
		sendSseEvent(subscriberList.lastSentEvent.getBytes(), query, outputStream);
		Logger.log("Added subscriber to query " + query);
	}

	private synchronized void removeSubscriber(IQuery query, OutputStream outputStream) {
		SubscriberList subscriberList = subscribersByQuery.get(query);
		subscriberList.subscribers.remove(outputStream);
		if (subscriberList.subscribers.size() == 0)
			subscribersByQuery.remove(query);
		Logger.log("Removed subscriber from query " + query);
	}

	private synchronized void onDomainModelUpdated() {
		for (IQuery query : subscribersByQuery.keySet()) {
			SubscriberList subscriberList = subscribersByQuery.get(query);
			String string = query.get(dataState);
			if (string.equals(subscriberList.lastSentEvent))
				continue;
			subscriberList.lastSentEvent = string;
			byte[] data = string.getBytes();
			for (OutputStream outputStream : subscriberList.subscribers) {
				executorService.submit(() -> sendSseEvent(data, query, outputStream));
			}
		}
	}

	private void sendSseEvent(byte[] data, IQuery query, OutputStream outputStream) {
		try {
			outputStream.write("data: ".getBytes());
			outputStream.write(data);
			outputStream.write("\n\n".getBytes());
			outputStream.flush();
			Logger.log("Sent query result " + query + " to subscriber " + outputStream);
		} catch (IOException exception) {
			removeSubscriber(query, outputStream);
		}
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
		for (IQuery query : subscribersByQuery.keySet()) {
			SubscriberList subscriberList = subscribersByQuery.get(query);
			for (OutputStream outputStream : subscriberList.subscribers) {
				try {
					outputStream.close();
				} catch (IOException e) {
					Logger.log("Got IOException when removing subscriber: " + e);
				}
			}
		}
	}

	private static class SubscriberList {
		public List<OutputStream> subscribers = new ArrayList<>();
		public String lastSentEvent = null;
	}

}