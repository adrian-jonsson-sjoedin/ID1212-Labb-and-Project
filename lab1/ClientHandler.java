package se.kth.id1212.labb1.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class handles the clients connecting to the server. It listens
 * to messages from other clients, and broadcast messages to all other
 * clients
 */
public class ClientHandler implements Runnable {

    private Socket connection;
    private static ArrayList<ClientHandler> clientList = new ArrayList<>();
    private BufferedReader incoming;
    private BufferedWriter outgoing;
    private String user;

    public ClientHandler(Socket connection, int user) {
        try {
            this.connection = connection;
            this.user = "User " + user;
            this.incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.outgoing = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            clientList.add(this);
            broadcast(this.user + " has joined the chat.");
        } catch (IOException exception) {
            System.err.println("Error creating Client Handler: " + exception);
            exception.printStackTrace();
        }
    }

    public void broadcast(String message) {
        if (isCloseConnectionRequestSent(message)) {
            closeConnection(this);
        }
        synchronized (clientList) {
            for (ClientHandler client : clientList) {
                try {
                    if (client.user != this.user) {
                        client.outgoing.write(message);
                        client.outgoing.flush();
                    }
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
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
            closeSocket(client.connection);
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

    private void closeWriter(BufferedWriter outgoing) throws IOException {
        if (outgoing != null) {
            outgoing.close();
        }
    }

    private void removeClient(ClientHandler client) {
        broadcast(client.user + " disconnected.");
        clientList.remove(client);
    }

    @Override
    public void run() {
//        try (BufferedWriter out = new BufferedWriter(
//                new OutputStreamWriter(
//                        this.connection.getOutputStream(),
//                        StandardCharsets.UTF_8));
//             BufferedReader incoming = new BufferedReader(new InputStreamReader(
//                     this.connection.getInputStream()))) {
//            String message;
//            while (!this.connection.isClosed()) {
//                message = incoming.readLine();
////                if (message.equals("/quit") || message.equals("/exit")) {
////                    break;
////                } else {
//                broadcast(message);
////                }
//            }
//        } catch (IOException ex) {
//            System.err.println("ClientHandler error: " + ex);
//        } finally {
//            if (this.connection != null) {
//                try {
//                    this.connection.close();
//                } catch (IOException ex) {
//                    System.err.println("Could not close client socket: " + ex);
//                }
//            }
//        }
        String message;
        while (!connection.isClosed()) {
            try {
                message = incoming.readLine();
                if (message != null)
                    broadcast(message);
            } catch (IOException exception) {
                System.err.println("Exception in run method: " + exception);
                break;
            }
        }
    }
}
