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
public class ClientHandler implements Runnable {

    private Socket connection;
    private static ArrayList<ClientHandler> clientList;
//    private BufferedReader incoming;

    public ClientHandler(Socket connection) {
        this.connection = connection;
    }

    public void updateClientList(ArrayList<ClientHandler> clientList) {
        ClientHandler.clientList = clientList;
    }

    public void broadcast(String message) {
        synchronized (clientList) {
            for (ClientHandler clientHandler : clientList) {
                try {
                    if (clientHandler.connection != this.connection) {
                        BufferedWriter out = new BufferedWriter(
                                new OutputStreamWriter(
                                        clientHandler.connection.getOutputStream(),
                                        StandardCharsets.UTF_8));
                        out.write(message);
                        out.flush();
                    }
                } catch (IOException ex) {
                }
            }
        }
    }

    @Override
    public void run() {
        try (BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(
                        this.connection.getOutputStream(),
                        StandardCharsets.UTF_8));
             BufferedReader incoming = new BufferedReader(new InputStreamReader(
                     this.connection.getInputStream()))) {
            String message;
            while (!this.connection.isClosed()) {
                message = incoming.readLine();
//                if (message.equals("/quit") || message.equals("/exit")) {
//                    break;
//                } else {
                broadcast(message);
//                }
            }
        } catch (IOException ex) {
            System.err.println("ClientHandler error: " + ex);
        } finally {
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
