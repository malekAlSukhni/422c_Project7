package assignment7;

import java.net.Socket;
import java.util.ArrayList;

public class ChatUser {
	
	String name;
	String password;
	boolean online = false;
	Socket clientSock = null;
	
	public ChatUser(String n, String p){
		name = n;
		password = p;
	}
	
	public void setSocket(Socket s){
		clientSock = s;
	}
}
