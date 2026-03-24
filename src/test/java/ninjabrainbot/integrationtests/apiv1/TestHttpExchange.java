package ninjabrainbot.integrationtests.apiv1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

class TestHttpExchange extends HttpExchange {

	private final String endpoint;
	public OutputStream outputStream = new ByteArrayOutputStream();
	private int responseCode = 0;

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
		return new Headers();
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
		responseCode = rCode;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return null;
	}

	@Override
	public int getResponseCode() {
		return responseCode;
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

