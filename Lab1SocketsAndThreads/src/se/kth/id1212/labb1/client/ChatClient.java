package se.kth.id1212.labb1.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * This class represents the client. It has two threads, one for
 * listening for incoming messages from the server, and one to send
 * messages to the server.
 */
public class ChatClient {
    private static final int PORT = 8418;
    private static final String HOST = "localhost";
    private Socket socket;
    private BufferedReader incoming;
    private PrintWriter outgoing;
    private boolean quitChat = false;

    public ChatClient() {
        try {
            this.socket = new Socket(HOST, PORT);
            //create read stream
            this.incoming = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            // create write stream
            this.outgoing = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException exception) {
            System.err.println("Client constructor error: " + exception);
        }

    }

    public synchronized void sendMessage() {
        try {
            Scanner scanner = new Scanner(System.in);
            String message;
            while (!quitChat && !socket.isClosed()) {
                message = scanner.nextLine();
//                outgoing.write(message);
                outgoing.println(message);
                outgoing.flush();
                if (message.startsWith("/quit")) {
                    System.out.println("Disconnecting from chat...");
                    quitChat = true;
                    break;
                }
            }
        } catch (Exception exception) {
            System.err.println("Client sendMessage error: " + exception);
        }
    }

    public synchronized void getMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String incomingMessage;
                while (!quitChat && !socket.isClosed()) {
                    try {
                        incomingMessage = incoming.readLine();
                        if (incomingMessage != null) {
                            System.out.println(incomingMessage);
                        }
                    } catch (IOException exception) {
                        System.err.println("Client getMessage error: " + exception);
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        System.out.println("Write a message then hit enter to send. Type /quit to exit the application.");
        ChatClient client = new ChatClient();
        client.getMessage();
        client.sendMessage();
    }
}