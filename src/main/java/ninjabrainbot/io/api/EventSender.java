package ninjabrainbot.io.api;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.util.Assert;
import ninjabrainbot.util.Logger;

public class EventSender implements IDisposable {

	private final IDataState dataState;
	private final HashMap<IQuery, List<OutputStream>> subscribersByQuery;
	private final DisposeHandler disposeHandler = new DisposeHandler();

	public EventSender(IDataState dataState, IDomainModel domainModel) {
		this.dataState = dataState;
		subscribersByQuery = new HashMap<>();

		disposeHandler.add(domainModel.whenModified().subscribe(this::onDomainModelUpdated));
	}

	public synchronized void addSubscriber(IQuery query, OutputStream outputStream) {
		if (!subscribersByQuery.containsKey(query))
			subscribersByQuery.put(query, new ArrayList<>());

		List<OutputStream> subscribers = subscribersByQuery.get(query);
		Assert.isFalse(subscribers.contains(outputStream));
		subscribers.add(outputStream);
		sendSseEvent(query.get(dataState).getBytes(), query, outputStream);
		Logger.log("Added subscriber to query " + query);
	}

	private synchronized void removeSubscriber(IQuery query, OutputStream outputStream) {
		List<OutputStream> subscribers = subscribersByQuery.get(query);
		subscribers.remove(outputStream);
		if (subscribers.size() == 0)
			subscribersByQuery.remove(query);
		Logger.log("Removed subscriber from query " + query);
	}

	private synchronized void onDomainModelUpdated() {
		for (IQuery query : subscribersByQuery.keySet()) {
			List<OutputStream> outputStreams = subscribersByQuery.get(query);
			byte[] data = query.get(dataState).getBytes();
			for (OutputStream outputStream : outputStreams) {
				sendSseEvent(data, query, outputStream);
			}
		}
	}

	private synchronized void sendSseEvent(byte[] data, IQuery query, OutputStream outputStream) {
		try {
			outputStream.write("data: ".getBytes());
			outputStream.write(data);
			outputStream.write("\n\n".getBytes());
			outputStream.flush();
		} catch (IOException exception) {
			removeSubscriber(query, outputStream);
		}
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
		for (IQuery query : subscribersByQuery.keySet()) {
			List<OutputStream> outputStreams = subscribersByQuery.get(query);
			for (OutputStream outputStream : outputStreams) {
				try {
					outputStream.close();
				} catch (IOException e) {
					Logger.log("Got IOException when removing subscriber: " + e);
				}
			}
		}
	}

}
