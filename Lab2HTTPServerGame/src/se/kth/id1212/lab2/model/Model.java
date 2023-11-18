package se.kth.id1212.lab2.model;

import java.util.HashMap;

/**
 * This class holds all the game sessions and manages them.
 */
public class Model {
    private HashMap<String, GameSession> gameSessions;

    public Model() {
        this.gameSessions = new HashMap<>();
    }

    /**
     * Add a new GameSession to the model
     * @param cookie Identifier for the client to whom the GameSession belongs to
     */
    public void addGameSession(String cookie) {
        this.gameSessions.put(cookie, new GameSession(cookie));
    }

    /**
     * Remove a GameSession from the model
     * @param cookie Identifier for the client to whom the GameSession belongs to
     */
    public void removeGameSession(String cookie) {
        this.gameSessions.remove(cookie);
    }

    /**
     * Retrieves a game session that is tied a specific cookie.
     * @param cookie Identifier for the client to whom the GameSession belongs to
     * @return The GameSession object
     */
    public GameSession getGameSession(String cookie) {
        return this.gameSessions.get(cookie);
    }
}
                                          