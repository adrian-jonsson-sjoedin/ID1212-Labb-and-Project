package se.kth.id1212.lab2.contoller;

import se.kth.id1212.lab2.model.GameSession;
import se.kth.id1212.lab2.model.Model;
import se.kth.id1212.lab2.view.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

;import static se.kth.id1212.lab2.view.HTLMView.*;

/**
 * This class acts as a controller for the application. It handles each new
 * incoming client and gives them an object of this class.
 * <p>
 * It manages incoming HTTP requests from the client browser, validates the
 * requests, checks for cookies and fetch relevant data from the model.
 */
public class RequestHandler implements Runnable {
    private Model model;
    private BufferedReader incoming;
    private PrintWriter outgoing;
    private Socket socket;
    private HTTPResponse view;

    /**
     * Initialize the request handler
     *
     * @param model  The applications model
     * @param socket The socket to which the client is connected to
     */
    public RequestHandler(Model model, Socket socket) {
        // Give the thread the application's model and save the clients socket
        this.model = model;
        this.socket = socket;
        // Create the read and write stream to between the client and server
        try {
            //create read stream
            this.incoming = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            // create write stream
            this.outgoing = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException exception) {
            System.err.println("[RequestHandler] Error while creating the handler. " +
                    exception.getMessage());
            closeConnection(this);
        }
        /* Initialize the class responsible for sending response and view back to client. Needs the stream to write to
           client*/
        this.view = new HTTPResponse(this.outgoing);
    }

    @Override
    public void run() {
        try {
            System.out.println("[RequestHandler run()]");
            HTTPRequest request = new HTTPRequest(this.incoming);
            int requestStatusCode = request.validateHTTPRequest();
            String requestMethod = request.getMethod();
            System.out.println("[RequestHandler run()] " + requestStatusCode); //debugging
            System.out.println("[RequestHandler run()] " + requestMethod); // debugging
            // we only want to do something if status is 200 OK
            if (requestStatusCode != 200) {

                handleErrorCodes(requestStatusCode);
                closeConnection(this);
                return;
            }
            System.out.println("[RequestHandler run()] calling handleGetRequest()"); //debugging
            if (requestMethod.equals("GET")) {
                handleGetRequest(request);
            }
        } catch (Exception exception) {
            System.err.println(exception);
            exception.printStackTrace();
        }
    }

    /**
     * This method handles a clients HTTP request when the method is GET.
     * Sets cookie if needed and add new game session if none exists.
     * @param request The clients HTTP request
     */
    private void handleGetRequest(HTTPRequest request) {
        System.out.println("[RequestHandler] In handleGetRequest()");
        String cookie = request.getCookie();
        GameSession currentGameSession = this.model.getGameSession(cookie);

        System.out.println("Cookie from GETMethod: " +cookie);
        // No cookie means new client connected. Generate cookie, add new game session to model
        if (cookie.equals("")) {
            System.out.println("[RequestHandler] No cookie found, generating new cookie and session");
            String newCookie = generateCookie();
            this.model.addGameSession(newCookie);
            //send server response to client through the view
            this.view.sendServerResponse(200, newCookie,
                    String.format(HTLMView.HTML_TEMPLATE, HTLMView.BODY_INITIAL));
            return;
        }

        // Cookie exists but there's no game session yet. Add new game session to model
        if (currentGameSession == null) {
            System.out.println("[RequestHandler] Cookie exists but no game session. Generating new game session");
            this.model.addGameSession(cookie);
            /* Send server response to client through the view.
               Sending an empty cookie here because we don't want the server to set a new cookie
               since one already exists */
            this.view.sendServerResponse(200, "",
                    String.format(HTLMView.HTML_TEMPLATE, HTLMView.BODY_INITIAL));
            return;
        }

        // Cookie and session exits, process the clients guess
        System.out.println("[RequestHandler] Cookie and session exists. Using existing session");
        String guess = extractUserGuess(request.getURI());
        if (guess.equals("INVALID_URI") || guess.equals("NOT_IN_RANGE") || guess.equals("NOT_A_NUMBER")) {
            sendView(currentGameSession.getNumbOfGuesses(), HTLMView.BODY_WRONG_INPUT);
            return;
        }
        String result = currentGameSession.guess(Integer.parseInt(guess));
        sendResult(result, currentGameSession, cookie);

    }

    /**
     * Method that decides upon which view to send to the browser depending on the results from the user's guess
     * @param result  The result String. Must be 'HIGHER', 'LOWER' or 'CORRECT'
     * @param session The current game session
     * @param cookie  The cookie for the current game session
     */
    private void sendResult(String result, GameSession session, String cookie) {
        switch (result) {
            case "HIGHER" -> sendView(session.getNumbOfGuesses(), BODY_TOO_LOW);
            case "LOWER" -> sendView(session.getNumbOfGuesses(), BODY_TOO_HIGH);
            case "CORRECT" -> {
                sendView(session.getNumbOfGuesses(), BODY_CORRECT);
                this.model.removeGameSession(cookie);
            }
        }
    }

    /**
     * Parse the URI from the browsers request to see if user has entered a valid number.
     *
     * @param uri The requested resource
     * @return The number the user guessed, or an appropriate error code
     */
    private String extractUserGuess(String uri) {
        // URI will be on the form /?guess=1
        System.out.println("[extractUserGuess()] " + uri);
        String userGuess;
        String[] guessURI = uri.split("=");
        try {
            if (guessURI.length != 2 && uri.startsWith("/?guess")) {
                userGuess = "INVALID_URI";
            } else if (Integer.parseInt(guessURI[1]) < 1 || Integer.parseInt(guessURI[1]) > 100) {
                userGuess = "NOT_IN_RANGE";
            } else {
                userGuess = guessURI[1];
            }
        } catch (NumberFormatException exception) {
            userGuess = "NOT_A_NUMBER";
        }
        System.out.println("[RequestHandler - extractUserGuess()] " + userGuess);
        return userGuess;
    }

    /**
     * Method that sends a view to the client that is displayed in the browser
     * @param numbOfGuesses The total amount of guesses a user has done.
     * @param template A string containing HTLM code where numbOfGuesses will be used
     */
    private void sendView(int numbOfGuesses, String template) {
        String body = String.format(template, numbOfGuesses);
        this.view.sendServerResponse(200, "", String.format(HTLMView.HTML_TEMPLATE, body));
    }

    /**
     * Generates a universally unique identifier.  Keep in mind that while the probability of
     * collisions with UUIDs is extremely low, it's not zero. But for this application it should
     * suffice.
     *
     * @return A unique UUID string
     */
    private String generateCookie() {
        UUID uuid = UUID.randomUUID();
        // Convert UUID to a string and remove hyphens
        String uniqueIdentifier = uuid.toString().replaceAll("-", "");
        return uniqueIdentifier;
    }
    //Not sure if I need this one. Think the method in HTTPRequest should be enough
    private void handleErrorCodes(int HTTPStatusCode) {

    }

    private synchronized void closeConnection(RequestHandler client) {
        try {
            closeReader(client.incoming);
            closeWriter(client.outgoing);
            closeSocket(client.socket);
        } catch (IOException exception) {
            System.err.println("[CloseConnection] error: " + exception);
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


}
