package ninjabrainbot.integrationtests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import org.junit.jupiter.api.Test;

public class ApiV1IntegrationTests {

	@Test
	void stronghold() throws IOException {
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/stronghold");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, executorService);

		apiV1HttpHandler.handle(exchange);

		System.out.println(exchange.getResponseBodyAsString());
	}

	private static class TestHttpExchange extends HttpExchange {

		private final String endpoint;
		public OutputStream outputStream = new ByteArrayOutputStream();

		public TestHttpExchange(String endpoint) {
			this.endpoint = endpoint;
		}

		public String getResponseBodyAsString() {
			return outputStream.toString();
		}

		@Override
		public Headers getRequestHeaders() {
			return null;
		}

		@Override
		public Headers getResponseHeaders() {
			return null;
		}

		@Override
		public URI getRequestURI() {
			try {
				return new URI(endpoint);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public String getRequestMethod() {
			return null;
		}

		@Override
		public HttpContext getHttpContext() {
			return null;
		}

		@Override
		public void close() {

		}

		@Override
		public InputStream getRequestBody() {
			return null;
		}

		@Override
		public OutputStream getResponseBody() {
			return outputStream;
		}

		@Override
		public void sendResponseHeaders(int rCode, long responseLength) throws IOException {

		}

		@Override
		public InetSocketAddress getRemoteAddress() {
			return null;
		}

		@Override
		public int getResponseCode() {
			return 0;
		}

		@Override
		public InetSocketAddress getLocalAddress() {
			return null;
		}

		@Override
		public String getProtocol() {
			return null;
		}

		@Override
		public Object getAttribute(String name) {
			return null;
		}

		@Override
		public void setAttribute(String name, Object value) {

		}

		@Override
		public void setStreams(InputStream i, OutputStream o) {

		}

		@Override
		public HttpPrincipal getPrincipal() {
			return null;
		}
	}

}
