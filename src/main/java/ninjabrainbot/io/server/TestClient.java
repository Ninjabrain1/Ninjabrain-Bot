package ninjabrainbot.io.server;

import java.io.IOException;
import java.net.Socket;

public class TestClient {

	public TestClient() {
		try {
			Socket socket = new Socket("localhost", 6666);
			socket.getOutputStream().write(123);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
