package se.kth.id1212.labb1.server;

import java.net.Socket;

/**
 * This class handles the clients connecting to the server. It listens
 * to messages from other clients, and broadcast messages to all other
 * clients
 */
public class ClientHandler implements Runnable{

    private Socket connection;

    public ClientHandler(Socket connection) {
        this.connection = connection;
    }




    @Override
    public void run() {

    }
}
