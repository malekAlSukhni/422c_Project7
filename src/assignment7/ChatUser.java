/* <ChatUser.java>
 * EE422C Project 7 submission by
 * Robert Bolt
 * rob329
 * 16465
 * Malek Al Sukhni
 * mha664
 * 16470
 * Slip days used: 1
 * Fall 2016
 */

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
