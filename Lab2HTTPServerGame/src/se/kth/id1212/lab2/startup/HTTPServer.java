package se.kth.id1212.lab2.startup;

import se.kth.id1212.lab2.contoller.RequestHandler;
import se.kth.id1212.lab2.model.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple HTTP server that runs on localhost. Has the main method that starts and runs
 * the whole application.
 */
public class HTTPServer {

    private final static int PORT = 8080; //static so main can use it.

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("[HTTPServer] Server has been started and is running on port " + server.getLocalPort());
            Model model = new Model(); // Initialize the model
            /**
             * The main thread listens for new connections and creates a new thread with a new game
             * session for each new individual client
             */
            while (true) {
                System.out.println("[HTTPServer] Listening for incoming connection requests...");
                Socket connection = server.accept();
                // To see what the browser is sending to the server
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line;
//                while ((line = br.readLine()) != null) {
//                    System.out.println("Client says: " + line);
//                }
                //-------------------------------------------------
                System.out.println("[HTTPServer] Accepted connection from " + connection.getInetAddress());
                System.out.println("[HTTPServer] Creating request handler for the client");
                // Create the request handler object and start the thread
                RequestHandler requestHandler = new RequestHandler(model, connection);
                Thread client = new Thread(requestHandler);
                client.start();

            }
        } catch (IOException exception) {
            System.err.println("[HTTPServer] Server error: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
