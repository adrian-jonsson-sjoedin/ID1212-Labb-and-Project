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

//        try (Socket socket = new Socket(HOST, PORT)) {
////            socket.setSoTimeout(15000);
//            System.out.println("Write a message then hit enter to send. Type /quit to exit the application.");
//
//            Thread clientReaderThread = new Thread(new ClientReader(socket));
//            Thread clientWriterThread = new Thread(new ClientWriter(socket));
//            clientReaderThread.start();
//            clientWriterThread.start();
//
//        } catch (UnknownHostException ex) {
//            System.err.println("Could not connect to server: " + ex);
//        } catch (IOException ex) {
//            System.err.println("I/O error: " + ex);
//        }
    }
}
//
///**
// * This class is responsible for reading incoming messages from the server and
// * displaying them to the client.
// */
//class ClientReader implements Runnable {
//    private Socket socket;
//    private BufferedReader incoming;
//
//    public ClientReader(Socket socket) {
//        this.socket = socket;
//        try {
//            this.incoming = new BufferedReader(
//                    new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
//        } catch (IOException ex) {
//            System.err.println("Error trying to get client input stream: " + ex);
//        }
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (!this.socket.isClosed() && this.socket != null) {
//                System.out.println(this.incoming.readLine());
//            }
//        } catch (IOException ex) { // not sure what this error would be because
//            System.err.println("From run method in ClientReader: " + ex);
//        } finally {
//            if (this.socket != null) {
//                try {
//                    this.socket.close();
//                } catch (IOException ex) {
//                    System.err.println("Could not close client socket: " + ex);
//                }
//            }
//        }
//    }
//}
//
///**
// * This class is responsible for reading user input from the console
// * and sending it to the server over the network
// */
//class ClientWriter implements Runnable {
//
//    private Socket socket;
//    private BufferedWriter outgoing; // used for sending the user input
//    private BufferedReader userInput; //for reading input from the user
//
//    public ClientWriter(Socket socket) {
//        this.socket = socket;
//        try {
//            this.outgoing = new BufferedWriter(
//                    new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8));
//            this.userInput = new BufferedReader(new InputStreamReader(System.in));
//        } catch (IOException ex) {
//            System.err.println("Error trying to get client output stream: " + ex);
//        }
//    }
//
//    @Override
//    public void run() {
//        String message;
//        try {
//            while (!this.socket.isOutputShutdown()) {
//                message = this.userInput.readLine();
//                if (message.isEmpty()) {
//                    continue;
//                }
//                this.outgoing.write(message);
//                this.outgoing.flush();
//                if (message.equals("/quit") || message.equals("/exit")) {
//                    break;
//                }
//            }
//        } catch (IOException ex) {
//            System.err.println(ex);
//        } finally {
//            if (this.socket != null) {
//                try {
//                    this.socket.close();
//                } catch (IOException ex) {
//                    System.err.println("Could not close client socket: " + ex);
//                }
//            }
//        }
//    }
//}
