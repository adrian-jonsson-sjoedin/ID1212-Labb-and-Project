package se.kth.id1212.labb1.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class represents the client. It has two threads, one for
 * listening for incoming messages from the server, and one to send
 * messages to the server.
 */
public class ChatClient {
    private static final int PORT = 8418;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT)) {
            socket.setSoTimeout(15000);
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
}

class ClientReader implements Runnable {

    private Socket socket;

    public ClientReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}

class ClientWriter implements Runnable {

    private Socket socket;

    public ClientWriter(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
