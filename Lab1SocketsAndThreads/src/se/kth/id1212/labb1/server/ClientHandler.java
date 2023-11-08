package se.kth.id1212.labb1.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This class handles the clients connecting to the server. It listens
 * to messages from other clients, and broadcast messages to all other
 * clients
 */
public class ClientHandler extends Thread {

    private final Socket connection;
    private final String userId;
    private static ArrayList<ClientHandler> clientList;
    private BufferedReader incoming;
    private BufferedWriter outgoing;

    public ClientHandler(Socket connection, int userId) throws IOException {
        this.connection = connection;
        this.userId = "User " + userId;
        this.incoming = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        this.outgoing = new BufferedWriter(
                new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
    }

    public void updateClientList(ArrayList<ClientHandler> clientList) {
        ClientHandler.clientList = clientList;
    }

    public void broadcast(String message) {
        synchronized (clientList) {
            ListIterator<ClientHandler> iterator = clientList.listIterator();
            ClientHandler current;
            while (iterator.hasNext()) {
                try {
                    current = iterator.next();
                    current.outgoing.write(message);
                    current.outgoing.flush();
                } catch (IOException ex) {
                    System.err.println("There was an error with sending the message: " + ex);
                }
            }
//            for (ClientHandler clientHandler : clientList) {
//                try {
//                    if (clientHandler.connection != this.connection) {
//                        BufferedWriter out = new BufferedWriter(
//                                new OutputStreamWriter(
//                                        clientHandler.connection.getOutputStream(),
//                                        StandardCharsets.UTF_8));
//                        out.write(message);
//                        out.flush();
//                    }
//                } catch (IOException ex) {
//                }
//            }
        }
    }

    @Override
    public void run() {
        try {
            String welcome = "Welcome to the chat " + this.userId + "!";
            this.outgoing.write(welcome);
            this.outgoing.flush();
            String message;
            while (!this.connection.isClosed()) {
                message = this.incoming.readLine();
                if (message.equals("/quit") || message.equals("/exit")) {
                    break;
                } else {
                    broadcast(this.userId + ": " + message);
                }
            }
        } catch (IOException ex) {
            System.err.println("ClientHandler error: " + ex);
            ex.printStackTrace();
        } finally {
            clientList.remove(this);
            System.out.println(this.userId + " has left the chat.");
            if (this.connection != null) {
                try {
                    this.connection.close();
                } catch (IOException ex) {
                    System.err.println("Could not close client socket: " + ex);
                }
            }
        }
    }
}
