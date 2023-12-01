/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
import java.util.Random;

/*
Networking: If you need to send instances of GameSession over a network (for 
example, in a client-server application), making the class serializable allows 
you to transmit the objects in a serialized form.

Session State in Web Applications: In a web application, you might want to store
the state of certain objects between different requests. Serialization allows 
you to store the object state and then restore it in subsequent requests.
*/

/**
 * One instance of the game session
 * @author adrian
 */
public class GameSession implements Serializable {
    private int numbOfGuesses;
    private int randomNumber;
    private final Random random;

    /**
     * Initialize the game session object
     *
     */
    public GameSession() {
        this.random = new Random();
        this.randomNumber = generateRandomNumber();
        this.numbOfGuesses = 0;
    }

    /**
     * Takes in a user's guess and compares it to the random number.
     * @param usersGuess The user's guess
     * @return CORRECT, HIGHER or LOWER
     */
    public String guess(String input) {
        if(input == null || input.isEmpty()){
            return "NO_GUESS";
        }
        int usersGuess = Integer.parseInt(input);
        if(usersGuess < 1 || usersGuess > 100){
            return "NOT_IN_RANGE";
        }
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

    public int getNumbOfGuesses() {
        return numbOfGuesses;
    }

    public int getRandomNumber() {
        return randomNumber;
    }
}
