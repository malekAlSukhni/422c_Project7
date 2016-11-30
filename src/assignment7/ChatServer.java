package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

public class ChatServer extends Observable {
	
	ArrayList<Socket> listOfAllSockets = new ArrayList<Socket>();

	public static void main(String[] args) {
		try {
			new ChatServer().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			listOfAllSockets.add(clientSocket);
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			System.out.println("got a connection");
		}
	}

	class ClientHandler implements Runnable {
		
		private String name = null;
		private Socket sock;
		private BufferedReader reader;

		public ClientHandler(Socket clientSocket) {
			sock = clientSocket;
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					String[] x = message.split(" ");
					System.out.println("server read " + message);
					switch (x[0]) {
					case "INIT":
						name = message.replace("INIT ", "");
						break;
					case "MESSAGE":
						setChanged();
						notifyObservers(message.replace("MESSAGE ", ""));
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
