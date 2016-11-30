package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class ChatServer extends Observable {

	ArrayList<Socket> listOfAllSockets = new ArrayList<Socket>();
	HashMap<String, String> info = new HashMap<String, String>();
	ArrayList<ChatUser> listOfUsers = new ArrayList<ChatUser>();

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
		private PrintWriter pw;

		public ClientHandler(Socket clientSocket) {
			sock = clientSocket;
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				pw = new PrintWriter(sock.getOutputStream());
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
					case "SU":
						if (info.keySet().contains(x[1])) {
							pw.println("error");
							pw.flush();
							break;
						}
						info.put(x[1], x[2]);
						listOfUsers.add(new ChatUser(x[1], x[2]));
						break;
					case "LOGIN":
						if (!info.keySet().contains(x[1])) {
							pw.println("error");
							pw.flush();
							break;
						}
						if (info.get(x[1]).equals(x[2])) {
							boolean dont = false;
							for (ChatUser u : listOfUsers) {
								if (u.name.equals(x[1])) {
									if (u.online) {
										pw.println("error");
										pw.flush();
										dont = true;
										break;
									} else {
										u.online = true;
									}
								}
							}
							if(dont){
								break;
							}
							pw.println("login success");
							pw.flush();
							break;
						} else {
							pw.println("error");
							pw.flush();
							break;
						}
					case "MESSAGE":
						setChanged();
						notifyObservers(message.replace("MESSAGE ", ""));
						break;
					case "LOGOUT":
						for (ChatUser u : listOfUsers) {
							if (u.name.equals(x[1])) {
								u.online = false;
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
