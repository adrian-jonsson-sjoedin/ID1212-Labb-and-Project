package se.kth.id1212.lab2.model;

import java.util.Random;

/**
 * One instance of the game session
 */
public class GameSession {
    private final String cookie; //to keep track of the sessions
    private int numbOfGuesses;
    private int randomNumber;
    private final Random random;

    /**
     * Initialize the game session object
     *
     * @param cookie The cookie set by the server
     */
    public GameSession(String cookie) {
        this.cookie = cookie;
        this.random = new Random();
        this.randomNumber = generateRandomNumber();
        this.numbOfGuesses = 0;
    }

    /**
     * Takes in a user's guess and compares it to the random number.
     * @param usersGuess The user's guess
     * @return CORRECT, HIGHER or LOWER
     */
    public String guess(int usersGuess) {
        this.numbOfGuesses++;
        if (usersGuess == this.randomNumber) {
            return "CORRECT";
        } else if (usersGuess < this.randomNumber) {
            return "HIGHER";
        } else return "LOWER";
    }

    public void setRandomNumberAndResetGuesses() {
        this.randomNumber = generateRandomNumber();
        this.numbOfGuesses = 0;
    }
    private int generateRandomNumber() {
        return (this.random.nextInt(100)+1);
    }

    public String getCookie() {
        return cookie;
    }

    public int getNumbOfGuesses() {
        return numbOfGuesses;
    }

    public int getRandomNumber() {
        return randomNumber;
    }
}
