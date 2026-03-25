package ninjabrainbot.io.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.api.interfaces.IQuery;
import ninjabrainbot.io.api.queries.AllAdvancementsQuery;
import ninjabrainbot.io.api.queries.BlindQuery;
import ninjabrainbot.io.api.queries.BoatQuery;
import ninjabrainbot.io.api.queries.DivineQuery;
import ninjabrainbot.io.api.queries.InformationMessagesQuery;
import ninjabrainbot.io.api.queries.PingQuery;
import ninjabrainbot.io.api.queries.StrongholdQuery;
import ninjabrainbot.io.api.queries.VersionQuery;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.model.input.IInputtedF3IToActionMapper;
import ninjabrainbot.model.input.IInputtedPlayerPositionToActionMapper;
import ninjabrainbot.util.Assert;
import ninjabrainbot.util.Logger;

public class ApiV1HttpHandler implements HttpHandler, IDisposable {

	private final EventSender eventSender;
	private final ApiV1CommandHandler commandHandler;
	private final HashMap<String, IQuery> queries;

	public ApiV1HttpHandler(IDataState dataState, IDomainModel domainModel, InformationMessageList informationMessageList, IActionExecutor actionExecutor, ExecutorService executorService, IInputtedPlayerPositionToActionMapper inputtedPlayerPositionToActionMapper, IInputtedF3IToActionMapper inputtedF3IToActionMapper) {
		eventSender = new EventSender(domainModel, executorService);
		commandHandler = new ApiV1CommandHandler(domainModel, dataState, actionExecutor, inputtedPlayerPositionToActionMapper, inputtedF3IToActionMapper);

		queries = new HashMap<>();
		queries.put("stronghold", new StrongholdQuery(dataState));
		queries.put("all-advancements", new AllAdvancementsQuery(dataState));
		queries.put("blind", new BlindQuery(dataState));
		queries.put("divine", new DivineQuery(dataState));
		queries.put("boat", new BoatQuery(dataState));
		queries.put("information-messages", new InformationMessagesQuery(informationMessageList));
		queries.put("version", new VersionQuery());
		queries.put("ping", new PingQuery());
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Logger.log("Received http request: " + exchange.getRequestURI());
		List<String> subdirectories = getSubdirectories(exchange);

		if (subdirectories.isEmpty()) {
			sendBadRequest(exchange, "Missing endpoint.");
			return;
		}

		if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			handlePost(exchange, subdirectories);
			return;
		}

		IQuery query = queries.getOrDefault(subdirectories.get(0), null);
		if (query == null) {
			sendBadRequest(exchange, "Unknown GET endpoint: " + subdirectories.get(0));
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

		sendBadRequest(exchange, "Invalid request.");
	}

	private void handlePost(HttpExchange exchange, List<String> subdirectories) throws IOException {
		if (subdirectories.size() != 1) {
			sendBadRequest(exchange, "Invalid command endpoint.");
			return;
		}

		String body = readRequestBody(exchange);
		ApiV1CommandHandler.Result result = commandHandler.handleCommandRequest(subdirectories.get(0), body);

		if (!result.success) {
			sendBadRequest(exchange, result.errorMessage);
			return;
		}

		sendOk(exchange);
	}

	private String readRequestBody(HttpExchange exchange) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}
	}

	private void sendOk(HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
		exchange.close();
	}

	private List<String> getSubdirectories(HttpExchange exchange) {
		String path = exchange.getRequestURI().getPath();
		Assert.isTrue(path.startsWith("/api/v1"));
		String[] subdirectories = path.substring(7).split("/");
		return Arrays.stream(subdirectories).filter(x -> !x.isEmpty()).collect(Collectors.toList());
	}

	private void sendBadRequest(HttpExchange exchange, String errorMessage) throws IOException {
		Logger.log("Bad request: " + errorMessage);
		byte[] responseBytes = ("{\"error\":\"" + errorMessage + "\"}").getBytes(StandardCharsets.UTF_8);
		exchange.getResponseHeaders().add("Content-Type", "application/json");
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, responseBytes.length);
		OutputStream os = exchange.getResponseBody();
		os.write(responseBytes);
		os.close();
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
