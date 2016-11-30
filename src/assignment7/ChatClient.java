package assignment7;

import java.io.*;
import java.net.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChatClient extends Application {

	private String name;
	private TextArea incoming = new TextArea();
	private TextField outgoing = new TextField();
	private BufferedReader reader;
	private PrintWriter writer;
	private Text error;
	private TextField tf;
	private TextField tf2;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		tf = new TextField();
		tf.setPrefColumnCount(5);
		tf.setPromptText("Enter Name:");
		tf.setLayoutX(10);
		tf.setLayoutY(10);
		tf.setPrefWidth(285);

		tf2 = new TextField();
		tf2.setPrefColumnCount(5);
		tf2.setPromptText("Enter Password:");
		tf2.setLayoutX(10);
		tf2.setLayoutY(50);
		tf2.setPrefWidth(285);

		btn1 = new Button();
		btn1.setText("Register");
		btn1.setLayoutX(305);
		btn1.setLayoutY(10);
		btn1.setOnAction(e -> setName(tf, tf2, 0));

		btn2 = new Button();
		btn2.setText("Login");
		btn2.setLayoutX(305);
		btn2.setLayoutY(50);
		btn2.setOnAction(e -> setName(tf, tf2, 1));

		error = new Text();
		error.setText("There was an error with the Signup/Login, please try again");
		error.setFill(Color.RED);
		error.setLayoutX(10);
		error.setLayoutY(120);
		error.setVisible(false);

		outgoing.setPrefColumnCount(5);
		outgoing.setPromptText("Enter Message");
		outgoing.setLayoutX(10);
		outgoing.setLayoutY(10);
		outgoing.setPrefWidth(285);
		outgoing.setVisible(false);

		btn3 = new Button();
		btn3.setText("Send");
		btn3.setLayoutX(305);
		btn3.setLayoutY(10);
		btn3.setOnAction(e -> sendMessage());
		btn3.setVisible(false);

		incoming.setLayoutX(10);
		incoming.setLayoutY(50);
		incoming.setPrefWidth(350);
		OutputStream out = new OutputStream() {
			public void write(int b) throws IOException {
				incoming.appendText(String.valueOf((char) b));
			}
		};
		System.setOut(new PrintStream(out, true));
		incoming.setVisible(false);
		
		btn4 = new Button();
		btn4.setText("Logout");
		btn4.setLayoutX(200);
		btn4.setLayoutY(350);
		btn4.setOnAction(e -> logout());
		btn4.setVisible(false);

		Pane pane = new Pane();
		pane.getChildren().addAll(tf, btn1, tf2, btn2, error, outgoing, incoming, btn3, btn4);

		primaryStage.setTitle("Chat Room");
		primaryStage.setScene(new Scene(pane, 450, 400));
		primaryStage.show();
		
		try {
			setUpNetworking();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void logout() {
		tf.setVisible(true);
		btn1.setVisible(true);
		tf2.setVisible(true);
		btn2.setVisible(true);
		error.setVisible(false);
		outgoing.setVisible(false);
		incoming.setVisible(false);
		btn3.setVisible(false);
		btn4.setVisible(false);
		outgoing.setText("LOGOUT " + name);
		sendText();
	}

	private void setName(TextField tf, TextField tf2, int val) {

		if (!tf.getText().equals("") && !tf2.getText().equals("")) {
			error.setVisible(false);
			if (val == 0) {
				outgoing.setText("SU " + tf.getText() + " " + tf2.getText());
				sendText();
			} else {
				name = tf.getText();
				outgoing.setText("LOGIN " + tf.getText() + " " + tf2.getText());
				sendText();
			}
		}
		tf.clear();
		tf2.clear();
	}

	private void sendMessage() {
		outgoing.setText("MESSAGE " + name + " : " + outgoing.getText());
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

	private void createMainStage() {

		tf.setVisible(false);
		btn1.setVisible(false);
		tf2.setVisible(false);
		btn2.setVisible(false);
		error.setVisible(false);
		outgoing.setVisible(true);
		incoming.setVisible(true);
		btn3.setVisible(true);
		btn4.setVisible(true);
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					if (message.equals("error")) {
						handleError();
					} else if (message.equals("login success")) {
						createMainStage();
					} else {
						incoming.appendText(message + "\n");
					}
				}
			} catch (IOException ex)

			{
				ex.printStackTrace();
			}
		}

		public void handleError() {
			error.setVisible(true);
		}
	}
}
