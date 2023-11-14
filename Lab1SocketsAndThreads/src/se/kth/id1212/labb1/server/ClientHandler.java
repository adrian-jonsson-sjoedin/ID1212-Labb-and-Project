package se.kth.id1212.labb1.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * This class handles the clients connecting to the server. It listens
 * to messages from other clients, and broadcast messages to all other
 * clients
 */
public class ClientHandler extends Thread {

    private final Socket socket;
    private final String userId;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private BufferedReader incoming;
    private BufferedWriter outgoing;

    public ClientHandler(Socket socket, int userId) throws IOException {
        this.socket = socket;
        this.userId = "User " + userId;
        //add the new client to the list of clients
        clients.add(this);
        //create read stream
        this.incoming = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        // create write stream
        this.outgoing = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        System.out.println("Number of users in chat: " + clients.size());
        //broadcast to everyone whenever a new user joins
        broadcast(this.userId + " has joined the chat!");
    }


    public synchronized void broadcast(String message) {
        for (ClientHandler client : clients) {
            try {
                if (!client.userId.equals(this.userId)) {
                    client.outgoing.write(message);
                    client.outgoing.flush();
                }
            } catch (IOException exception) {
                System.err.println("Error in ClientHandler broadcast: " + exception);
            }
        }
    }

    @Override
    public void run() {
        String message;
        while (!socket.isClosed()) {
            try {
                message = incoming.readLine();
                broadcast(message);
            } catch (IOException exception) {
                System.err.println("ClientHandler run method: " + exception);
            }
        }
    }
}
