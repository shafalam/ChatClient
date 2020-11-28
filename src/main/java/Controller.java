package main.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {
    Socket socket;
    BufferedReader reader;
    ObservableList<String> allMessages;
    @FXML
    private Label label;
    @FXML
    public ListView<String> messages;
    @FXML
    private TextField textMessage;
    @FXML
    private Button sendButton;

    @FXML
    public void initialize() {
        // label = new Label("Hey");
        allMessages = FXCollections.observableArrayList();
        messages.getItems().addAll(allMessages);

        try {
            socket = new Socket("localhost", 5000);
        } catch (IOException e) {
            System.out.println("IOException occurred: " + e.getMessage());
        }

        Client aClient = new Client();
        aClient.start();
    }

    public void add(String message) {
        int i = 0;
        allMessages.add("Love you no matter what " + i++);
        messages.getItems().add(message);
        System.out.println("Pressed");
    }

    @FXML
    private void sendMessage() {
        new Client().send();
    }

    class Client extends Thread {
        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while (true) {
                    message = reader.readLine();
                    add(message);
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("IOException occurred: " + e.getMessage());
            }
        }

        public void send() {
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                String message = textMessage.getText();
                writer.println(message);
                writer.flush();

            } catch (IOException e) {
                System.out.println("Exception occurred while sending data: " + e.getMessage());
            } finally {
                textMessage.setText("");
            }
        }
    }
}
