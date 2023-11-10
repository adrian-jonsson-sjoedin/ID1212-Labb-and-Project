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

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            // this listens for new connections and spawns a new thread per connection
            while (true) {
                try {
                    System.out.println("Listening for incoming connection requests...");
                    Socket connection = server.accept();
                    System.out.println("Accepted connection from " + connection.getInetAddress());
                    System.out.println("Accepted connection from " + connection.getPort());
                    ClientHandler clientHandler = new ClientHandler(connection, connection.getPort());
//                    clientList.add(clientHandler);
//                    clientHandler.updateClientList(clientList);
                    Thread client = new Thread(clientHandler);
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

