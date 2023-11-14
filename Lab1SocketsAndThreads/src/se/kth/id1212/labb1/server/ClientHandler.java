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
    private PrintWriter outgoing;

    public ClientHandler(Socket socket, int userId) throws IOException {
        this.socket = socket;
        this.userId = "User " + userId;
        //add the new client to the list of clients
        clients.add(this);
        //create read stream
        this.incoming = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        // create write stream
        this.outgoing = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        System.out.println("Number of users in chat: " + clients.size());
        //broadcast to everyone whenever a new user joins
        broadcast(this.userId + " has joined the chat!");
    }


    public synchronized void broadcast(String message) {
        if (isCloseConnectionRequestSent(message)) {
            closeConnection(this);
        }
        for (ClientHandler client : clients) {
            try {
                System.out.println("Inside broadcast method");
                if (!client.userId.equals(this.userId)) {
                    client.outgoing.println(this.userId + ": " + message);
                    client.outgoing.flush();
                }
            } catch (Exception exception) {
                System.err.println("Error in ClientHandler broadcast: " + exception);
            }
//            catch (IOException exception) {
//                System.err.println("Error in ClientHandler broadcast: " + exception);
//            }
        }
    }

    public boolean isCloseConnectionRequestSent(String msg) {
        if (msg.startsWith("/quit")) {
            return true;
        } else {
            return false;
        }
    }

    private synchronized void closeConnection(ClientHandler client) {
        try {
            closeReader(client.incoming);
            closeWriter(client.outgoing);
            closeSocket(client.socket);
            removeClient(client);
        } catch (IOException exception) {
            System.err.println("Error trying to close in Client handler: " + exception);
            exception.printStackTrace();
        }
    }

    private void closeSocket(Socket client) throws IOException {
        if (client != null) {
            client.close();
        }
    }

    private void closeReader(BufferedReader incoming) throws IOException {
        if (incoming != null) {
            incoming.close();
        }
    }

    private void closeWriter(PrintWriter outgoing) throws IOException {
        if (outgoing != null) {
            outgoing.close();
        }
    }

    private void removeClient(ClientHandler client) {
        broadcast(client.userId + " disconnected.");
        clients.remove(client);
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
