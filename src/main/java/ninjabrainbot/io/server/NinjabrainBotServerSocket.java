package ninjabrainbot.io.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NinjabrainBotServerSocket implements Runnable {

	private ServerSocket serverSocket;

	public NinjabrainBotServerSocket(){
		try {
			serverSocket = new ServerSocket(6666);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		try {
			Socket clientSocket = serverSocket.accept();
			int data = clientSocket.getInputStream().read();
			System.out.println("SERVER: " + data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
