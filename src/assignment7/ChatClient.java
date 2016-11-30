package assignment7;

import java.io.*;
import java.net.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ChatClient extends Application{
	
	private ChatUser user;
	private TextArea incoming;
	private TextField outgoing;
	private BufferedReader reader;
	private PrintWriter writer;
	private Stage primaryStage;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		
		TextField tf = new TextField();
		tf.setPrefColumnCount(5);
		tf.setPromptText("Enter Name:");
		tf.setLayoutX(10);
		tf.setLayoutY(10);
		tf.setPrefWidth(285);
		
		Button btn1 = new Button();
		btn1.setText("Confirm");
		btn1.setLayoutX(305);
		btn1.setLayoutY(10);
		btn1.setOnAction(e -> setName(tf));
		
		Pane pane1 = new Pane();
		pane1.getChildren().addAll(tf, btn1);
		
		primaryStage.setTitle("Name Select");
		primaryStage.setScene(new Scene(pane1, 400, 50));
		primaryStage.show();
		

	}

	private void setName(TextField tf) {
		
		if(!tf.getText().equals("")){
		user = new ChatUser(tf.getText());

		outgoing = new TextField();
		outgoing.setPrefColumnCount(5);
		outgoing.setPromptText("Enter Message");
		outgoing.setLayoutX(10);
		outgoing.setLayoutY(10);
		outgoing.setPrefWidth(285);
		
		Button btn2 = new Button();
		btn2.setText("Send");
		btn2.setLayoutX(305);
		btn2.setLayoutY(10);
		btn2.setOnAction(e -> sendMessage());
		
		incoming = new TextArea();
		incoming.setLayoutX(10);
		incoming.setLayoutY(50);
		incoming.setPrefWidth(350);
		OutputStream out = new OutputStream() {
			public void write(int b) throws IOException {
				incoming.appendText(String.valueOf((char) b));
			}
		};
		System.setOut(new PrintStream(out, true));
		
		Pane pane2 = new Pane();
		pane2.getChildren().addAll(outgoing, incoming, btn2);
		
		try {
			setUpNetworking();
			outgoing.setText("INIT " + tf.getText());
			sendText();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		primaryStage.setTitle(user.name);
		primaryStage.setScene(new Scene(pane2, 450, 400));
		}
	}

	private void sendMessage() {
		outgoing.setText("MESSAGE " + user.name + " : " + outgoing.getText());
		sendText();
	}

	private void sendText() {
		writer.println(outgoing.getText());
		writer.flush();
		outgoing.setText("");
		outgoing.requestFocus();
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("localhost", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("Joined Chat Lobby");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					
						incoming.appendText(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
