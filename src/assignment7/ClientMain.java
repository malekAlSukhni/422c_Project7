package assignment7;

import java.net.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;

import java.io.*;
import java.applet.*;
import java.awt.*;

public class ClientMain extends Application{
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;

	
	@Override
	public void start(Stage primaryStage){
		BorderPane textPane = new BorderPane();
		textPane.setPadding(new Insets(5, 5, 5, 5));
		textPane.setStyle("-fx-border-color: green");
		textPane.setLeft(new Label("Enter message:"));
		
		TextField nM =  new TextField();
		nM.setAlignment(Pos.BOTTOM_RIGHT);
		textPane.setCenter(nM);
		
		BorderPane centerPane = new BorderPane();
		TextArea display = new TextArea();
		centerPane.setCenter(new ScrollPane(display));
		centerPane.setTop(textPane);
		
		Scene scene = new Scene(centerPane, 450, 400);
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	
	public static void main(String[] args) {
		launch(args);
		}
}
