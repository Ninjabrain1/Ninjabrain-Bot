package ninjabrainbot.io.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.util.Logger;

public class NinjabrainBotHttpServer implements IDisposable {

	private final IDataState dataState;
	private final IDomainModel domainModel;
	private final NinjabrainBotPreferences preferences;
	private final DisposeHandler disposeHandler = new DisposeHandler();

	private HttpServer httpServer;
	private ApiV1HttpHandler apiV1HttpHandler;
	private ExecutorService executorService;
	private Exception error;

	public NinjabrainBotHttpServer(IDataState dataState, IDomainModel domainModel, NinjabrainBotPreferences preferences) {
		this.dataState = dataState;
		this.domainModel = domainModel;
		this.preferences = preferences;
		updateHttpServerStatus();

		disposeHandler.add(preferences.enableHttpServer.whenModified().subscribe(this::updateHttpServerStatus));
	}

	private void updateHttpServerStatus() {
		if (preferences.enableHttpServer.get()) {
			if (httpServer == null)
				startHttpServer(52533);
		} else {
			if  (httpServer != null)
				stopHttpServer();
		}
	}

	private void startHttpServer(int port){
		error = null;
		try {
			httpServer = HttpServer.create(new InetSocketAddress(port), 0);
		} catch (IOException e) {
			Logger.log(e.toString());
			error = e;
			return;
		}
		if (executorService == null)
			executorService = Executors.newFixedThreadPool(1);
		apiV1HttpHandler = new ApiV1HttpHandler(dataState, domainModel, executorService);
		httpServer.createContext("/api/v1", apiV1HttpHandler);
		httpServer.setExecutor(executorService);
		httpServer.start();
		Logger.log("HTTP server started on port " + port);
	}

	private void stopHttpServer(){
		apiV1HttpHandler.dispose();
		apiV1HttpHandler = null;
		httpServer.stop(0);
		httpServer = null;
		Logger.log("HTTP server stopped");
	}

	@Override
	public void dispose() {
		if (httpServer != null) {
			httpServer.stop(0);
		}
		if (apiV1HttpHandler != null){
			apiV1HttpHandler.dispose();
		}
		disposeHandler.dispose();
	}
}

