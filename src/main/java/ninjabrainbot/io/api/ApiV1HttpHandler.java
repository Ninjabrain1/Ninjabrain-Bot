package ninjabrainbot.io.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.IClipboardListener;
import ninjabrainbot.io.api.actions.IApiAction;
import ninjabrainbot.io.api.actions.InputClipboardApiAction;
import ninjabrainbot.io.api.actions.InputKeysApiAction;
import ninjabrainbot.io.api.queries.AllAdvancementsQuery;
import ninjabrainbot.io.api.queries.BlindQuery;
import ninjabrainbot.io.api.queries.BoatQuery;
import ninjabrainbot.io.api.queries.CalculatorStateQuery;
import ninjabrainbot.io.api.queries.DivineQuery;
import ninjabrainbot.io.api.queries.IQuery;
import ninjabrainbot.io.api.queries.InformationMessagesQuery;
import ninjabrainbot.io.api.queries.PingQuery;
import ninjabrainbot.io.api.queries.StrongholdQuery;
import ninjabrainbot.io.api.queries.VersionQuery;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.util.Assert;
import ninjabrainbot.util.Logger;

public class ApiV1HttpHandler implements HttpHandler, IDisposable {

	private final EventSender eventSender;
	private final HashMap<String, IQuery> queries;
	private final HashMap<String, IApiAction> actions;

	public ApiV1HttpHandler(IDataState dataState, IDomainModel domainModel, InformationMessageList informationMessageList, ExecutorService executorService, IClipboardListener clipboardListener) {
		eventSender = new EventSender(domainModel, executorService);

		queries = new HashMap<>();
		queries.put("stronghold", new StrongholdQuery(dataState));
		queries.put("all-advancements", new AllAdvancementsQuery(dataState));
		queries.put("blind", new BlindQuery(dataState));
		queries.put("divine", new DivineQuery(dataState));
		queries.put("boat", new BoatQuery(dataState));
		queries.put("calc-state", new CalculatorStateQuery(dataState));
		queries.put("information-messages", new InformationMessagesQuery(informationMessageList));
		queries.put("version", new VersionQuery());
		queries.put("ping", new PingQuery());

		actions = new HashMap<>();
		actions.put("input-keys", new InputKeysApiAction());
		actions.put("input-clipboard", new InputClipboardApiAction(clipboardListener));
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Logger.log("Received request: " + exchange.getRequestURI());
		List<String> subdirectories = getSubdirectories(exchange);

		if (subdirectories.size() == 0) {
			sendBadRequest(exchange);
			return;
		}
		if ("get".equalsIgnoreCase(exchange.getRequestMethod())) {
			IQuery query = queries.getOrDefault(subdirectories.get(0), null);
			if (query == null) {
				sendBadRequest(exchange);
				return;
			}

			if (subdirectories.size() == 1) {
				sendQueryResponse(exchange, query);
				return;
			}

			if (query.supportsSubscriptions() && subdirectories.size() == 2 && subdirectories.get(1).contentEquals("events")) {
				subscribeToQuery(exchange, query);
				return;
			}
		} else if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
			IApiAction action = actions.getOrDefault(subdirectories.get(0), null);
			if (action == null) {
				sendBadRequest(exchange);
				return;
			}

			if (subdirectories.size() == 1) {
				sendActionResponse(exchange, action);
				return;
			}
		}

		sendBadRequest(exchange);
	}

	private List<String> getSubdirectories(HttpExchange exchange) {
		String path = exchange.getRequestURI().getPath();
		Assert.isTrue(path.startsWith("/api/v1"));
		String[] subdirectories = path.substring(7).split("/");
		return Arrays.stream(subdirectories).filter(x -> x.length() != 0).collect(Collectors.toList());
	}

	private void sendBadRequest(HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
		exchange.getResponseBody().close();
	}

	private void sendQueryResponse(HttpExchange exchange, IQuery query) {
		try {
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.add("Access-Control-Allow-Origin", "*");

			OutputStream outputStream = exchange.getResponseBody();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			outputStream.write(query.get().getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			Logger.log("HTTP server failed to send query response: " + e);
		}
	}

	private void sendActionResponse(HttpExchange exchange, IApiAction action) {
		try {
			try {
				String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).lines()
						.collect(Collectors.joining(System.lineSeparator()));
				String response = action.post(body);

				Headers responseHeaders = exchange.getResponseHeaders();
				responseHeaders.add("Access-Control-Allow-Origin", "*");

				OutputStream outputStream = exchange.getResponseBody();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				outputStream.write(response.getBytes());
				outputStream.flush();
				outputStream.close();
			} catch (RuntimeException e) {
				Logger.log("Invalid request handling: " + e);
				sendBadRequest(exchange);
			}
		} catch (IOException e) {
			Logger.log("HTTP server failed to send action response: " + e);
		}
	}

	private void subscribeToQuery(HttpExchange exchange, IQuery query) {
		try {
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.add("Content-Type", "text/event-stream");
			responseHeaders.add("Connection", "keep-alive");
			responseHeaders.add("Transfer-Encoding", "chunked");
			responseHeaders.add("Access-Control-Allow-Origin", "*");

			OutputStream outputStream = exchange.getResponseBody();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			eventSender.addSubscriber(query, outputStream);
		} catch (IOException e) {
			Logger.log("HTTP server failed to send query subscription response: " + e);
		}
	}

	@Override
	public void dispose() {
		eventSender.dispose();
	}
}
