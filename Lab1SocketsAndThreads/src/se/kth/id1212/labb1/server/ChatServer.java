package se.kth.id1212.labb1.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class represents the server. Has one thread for each of
 * the clients currently connected, and one for listening for
 * incoming connections from new clients.
 */
public class ChatServer {

    private final static int PORT = 8418;
    private static ArrayList<ClientHandler> clientList = new ArrayList<>(); // Holds all active client threads

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            // this listens for new connections and spawns a new thread per connection
            System.out.println("Server has been started and is running on port "+ server.getLocalPort());
            while (true) {
                try {
                    System.out.println("Listening for incoming connection requests...");
                    // Listens for a connection to be made to this socket and accepts it.
                    // The method blocks until a connection is made
                    Socket connection = server.accept();
                    System.out.println("Accepted connection from " + connection.getInetAddress());
                    System.out.println("Creating client handler for the client");
                    ClientHandler client = new ClientHandler(connection, connection.getPort());
//                    clientList.add(client); //add client thread to the array list
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

