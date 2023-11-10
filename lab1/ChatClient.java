package se.kth.id1212.labb1.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * This class represents the client. It has two threads, one for
 * listening for incoming messages from the server, and one to send
 * messages to the server.
 */
public class ChatClient {
    private static final int PORT = 8418;
    private static final String HOST = "localhost";
    String user;


    public ChatClient() {
        try (Socket socket = new Socket(HOST, PORT)) {
            this.user = "User " + socket.getLocalPort();
            Thread clientReaderThread = new Thread(new ClientReader(socket));
            Thread clientWriterThread = new Thread(new ClientWriter(socket));
            clientReaderThread.start();
            clientWriterThread.start();

        } catch (UnknownHostException ex) {
            System.err.println("Could not connect to server: " + ex);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
    }

    public static void main(String[] args) {
//        try (Socket socket = new Socket(HOST, PORT)) {
//            this.user = "User " + socket.getLocalPort();
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
        ChatClient client = new ChatClient();
    }

    private static void closeSocket(Socket client) throws IOException {
        if (client != null && !client.isClosed()) {
            client.close();
        }
    }

    private static void closeReader(BufferedReader incoming) throws IOException {
        if (incoming != null) {
            incoming.close();
        }
    }

    private static void closeWriter(BufferedWriter outgoing) throws IOException {
        if (outgoing != null) {
            outgoing.close();
        }
    }

    public static void closeConnection(Socket client, BufferedReader incoming, BufferedWriter outgoing) {
        try {
            closeReader(incoming);
            closeWriter(outgoing);
            closeSocket(client);
        } catch (IOException ex) {
            System.err.println("Error while trying to close client connection: " + ex);
        }

    }
}

/**
 * This class is responsible for reading incoming messages from the server and
 * displaying them to the client.
 */
class ClientReader implements Runnable {
    private Socket socket;
    private BufferedReader incoming;

    public ClientReader(Socket socket) {
        this.socket = socket;
        try {
            this.incoming = new BufferedReader(
                    new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException ex) {
            System.err.println("Error trying to get client input stream: " + ex);
        }

    }

    @Override
    public void run() {
        String incomingMessage;
        System.out.println("Before the reader while loop");
        while (!this.socket.isClosed() && this.socket != null) {
            System.out.println("In the client reader while loop");
            try {
                incomingMessage = this.incoming.readLine();
//                if (incomingMessage != null) {
                    System.out.println(incomingMessage);
//                }
            } catch (IOException ex) { // not sure what this error would be because
                System.err.println("Lost Connection to server: " + ex);
//                closeConnection(socket, incoming, null);
            }
        }
//        } finally {
//            if (this.socket != null) {
//                try {
//                    this.socket.close();
//                } catch (IOException ex) {
//                    System.err.println("Could not close client socket: " + ex);
//                }
//            }
//            closeConnection(this.socket, this.incoming, null);
//        }
    }
}

/**
 * This class is responsible for reading user input from the console
 * and sending it to the server over the network
 */
class ClientWriter implements Runnable {

    private Socket socket;
    private BufferedWriter outgoing; // used for sending the user input
    private BufferedReader userInput; //for reading input from the user

    public ClientWriter(Socket socket) {
        this.socket = socket;
        try {
            this.outgoing = new BufferedWriter(
                    new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8));
            this.userInput = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException ex) {
            System.err.println("Error trying to get client output stream: " + ex);
        }
    }

    @Override
    public void run() {
        String message;
        try {
            System.out.println("Before the client writer loop");
            while (!this.socket.isClosed() && this.socket != null) {
                System.out.println("In the client writer loop");
                message = this.userInput.readLine();
                if (message.isEmpty()) {
                    continue;
                }
                this.outgoing.write(message);
                this.outgoing.flush();
                if (message.startsWith("/quit")) {
                    System.out.println("Disconnecting...");
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println(ex);
        } finally {
//            if (this.socket != null) {
//                try {
//                    this.socket.close();
//                } catch (IOException ex) {
//                    System.err.println("Could not close client socket: " + ex);
//                }
//            }
//            closeConnection(this.socket, this.userInput, this.outgoing);
        }
    }
}
