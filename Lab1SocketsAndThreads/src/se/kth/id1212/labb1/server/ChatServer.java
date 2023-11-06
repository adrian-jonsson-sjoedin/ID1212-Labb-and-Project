package se.kth.id1212.labb1.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class represents the server. Has one thread for each of
 * the clients currently connected, and one for listening for
 * incoming connections from new clients.
 */
public class ChatServer {

    private final static int PORT = 8418;
    private static ArrayList<Thread> clientList = new ArrayList<>(); // Holds all active client threads

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            // this listens for new connections and spawns a new thread per connection
            while (true) {
                try {
                    System.out.println("Listening for incoming connection requests...");
                    Socket connection = server.accept();
                    System.out.println("Accepted connection from " + connection.getInetAddress());
                    ClientHandler clientHandler = new ClientHandler(connection);
                    Thread client = new Thread(clientHandler);
                    clientList.add(client);
                    client.start();
                } catch (IOException exception) {
                    System.err.println("Error accepting connection: " + exception);
                }
            }
        } catch (IOException ex) {
            System.err.println("Couldn't start server: " + ex);
        }
    }
}

