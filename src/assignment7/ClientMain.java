/* <ClientMain.java>
 * EE422C Project 7 submission by
 * Robert Bolt
 * rob329
 * 16465
 * Malek Al Sukhni
 * mha664
 * 16470ow
 * Slip days used: 1
 * Fall 2016
 */
package assignment7;

import java.io.*;
import java.net.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientMain extends Application {

	private String name;
	private TextArea incoming = new TextArea();
	private TextArea incomingPrivate = new TextArea();
	private TextField outgoing = new TextField();
	private BufferedReader reader;
	private PrintWriter writer;
	private Text error;
	private Text error1;
	private Text success;
	private TextField tf;
	private PasswordField tf2;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private OutputStream out;
	private OutputStream out2;
	private Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;

		tf = new TextField();
		tf.setPrefColumnCount(5);
		tf.setPromptText("Enter Name:");
		tf.setLayoutX(82.5);
		tf.setLayoutY(30);
		tf.setPrefWidth(285);

		tf2 = new PasswordField();
		tf2.setPrefColumnCount(5);
		tf2.setPromptText("Enter Password:");
		tf2.setLayoutX(82.5);
		tf2.setLayoutY(70);
		tf2.setPrefWidth(285);

		btn1 = new Button();
		btn1.setText("Register");
		btn1.setLayoutX(185);
		btn1.setLayoutY(110);
		btn1.setOnAction(e -> setName(tf, tf2, 0));

		btn2 = new Button();
		btn2.setText("Login");
		btn2.setLayoutX(195);
		btn2.setLayoutY(150);
		btn2.setOnAction(e -> setName(tf, tf2, 1));

		error = new Text();
		error.setText("There was an error with the Signup/Login, please try again");
		error.setFill(Color.RED);
		error.setLayoutX(30);
		error.setLayoutY(210);
		error.setVisible(false);
		
		error1 = new Text();
		error1.setText("There was an error with sending your message, please try again");
		error1.setFill(Color.RED);
		error1.setLayoutX(10);
		error1.setLayoutY(300);
		error1.setVisible(false);
		
		success = new Text();
		success.setText("Signup Success");
		success.setFill(Color.GREEN);
		success.setLayoutX(180);
		success.setLayoutY(210);
		success.setVisible(false);

		outgoing.setPrefColumnCount(5);
		outgoing.setPromptText("Enter Message");
		outgoing.setLayoutX(50);
		outgoing.setLayoutY(10);
		outgoing.setPrefWidth(285);
		outgoing.setVisible(false);
		outgoing.setOnAction(e -> sendMessage());

		btn3 = new Button();
		btn3.setText("Send");
		btn3.setLayoutX(345);
		btn3.setLayoutY(10);
		btn3.setOnAction(e -> sendMessage());
		btn3.setVisible(false);

		incoming.setLayoutX(50);
		incoming.setLayoutY(50);
		incoming.setPrefWidth(350);
		out = new OutputStream() {
			public void write(int b) throws IOException {
				incoming.appendText(String.valueOf((char) b));
			}
		};
		System.setOut(new PrintStream(out, true));
		incoming.setVisible(false);
		
		incomingPrivate.setLayoutX(10);
		incomingPrivate.setLayoutY(50);
		incomingPrivate.setPrefWidth(350);
		out2 = new OutputStream() {
			public void write(int b) throws IOException {
				incoming.appendText(String.valueOf((char) b));
			}
		};
		System.setOut(new PrintStream(out, true));
		incomingPrivate.setVisible(false);
		
		btn4 = new Button();
		btn4.setText("Logout");
		btn4.setLayoutX(200);
		btn4.setLayoutY(350);
		btn4.setOnAction(e -> logout());
		btn4.setVisible(false);

		Pane pane = new Pane();
		pane.setBackground(new Background(new BackgroundFill(Color.DARKTURQUOISE, CornerRadii.EMPTY, Insets.EMPTY)));
		pane.getChildren().addAll(tf, btn1, tf2, btn2, error, error1, success, outgoing, incoming, btn3, btn4, incomingPrivate);

		this.primaryStage.setTitle("Chat Room");
		this.primaryStage.setScene(new Scene(pane, 450, 400));
		this.primaryStage.show();
		
		try {
			setUpNetworking();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	private void logout() {
		tf.setVisible(true);
		btn1.setVisible(true);
		tf2.setVisible(true);
		btn2.setVisible(true);
		outgoing.setVisible(false);
		incoming.setVisible(false);
		btn3.setVisible(false);
		btn4.setVisible(false);
		outgoing.setText("LOGOUT " + name);
		disableErrors();
		sendText();
	}

	private void setName(TextField tf, TextField tf2, int val) {

		if (!tf.getText().equals("") && !tf2.getText().equals("")) {
			disableErrors();
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
		String[] message = outgoing.getText().split(" ");
		if(message[0].equals("/private")){
			String privateMessage = outgoing.getText();
			privateMessage = privateMessage.replace("/private", "");
			outgoing.setText("PRIVATE" + privateMessage);
			sendText();
			return;
		}
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
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}

	private void createMainStage() {

		tf.setVisible(false);
		btn1.setVisible(false);
		tf2.setVisible(false);
		btn2.setVisible(false);
		outgoing.setVisible(true);
		incoming.setVisible(true);
		btn3.setVisible(true);
		btn4.setVisible(true);
		outgoing.clear();
		incoming.clear();
		disableErrors();
	}
	
	private void handleError(String[] x) {
		success.setVisible(false);
		switch(x[1]){
		case "0":
			error.setVisible(true);
			break;
		case "1":
			error1.setVisible(true);
			break;
		}
	}
	
	private void disableErrors(){
		success.setVisible(false);
		error.setVisible(false);
		error1.setVisible(false);
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					String[] x = message.split(" ");
					if (x[0].equals("error")) {
						handleError(x);
					} else if (message.equals("login success")) {
						createMainStage();
					} else if (message.equals("signup")){
						success.setVisible(true);
					}
					else {
						disableErrors();
						incoming.appendText(message + "\n");
					}
				}
			} catch (IOException ex)

			{
				ex.printStackTrace();
			}
		}
	}
}
