package assignment7;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ServerMain{

	public static void main(String[] args) {
		try{
		ServerSocket myService = new ServerSocket(4000);

		while (true) {
			Socket x = myService.accept();
			DataInputStream input = new DataInputStream(x.getInputStream());
			PrintStream output = new PrintStream(x.getOutputStream());
			output.println(input.readUTF());		
		}
		}
		catch(Exception e){
			System.out.println("Something went wrong");
		}
	}
	
	public static class Client implements Runnable{
		
		Socket clientSocket;
		
		public void Client(Socket x){
			clientSocket = x;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}



}
