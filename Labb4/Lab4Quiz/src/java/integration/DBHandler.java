/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import model.Question;
import model.Quiz;
import model.User;
import org.apache.catalina.tribes.util.Arrays;

/**
 *
 * @author adrian
 */
public class DBHandler {

    private Connection connection;
    private PreparedStatement getUser;
    private PreparedStatement getAllQuizzes;
    private PreparedStatement getQuizQuestions;
    private PreparedStatement getResults;
    private PreparedStatement updateResult;

    public DBHandler() {
        try {
            //Establish a connection to the db
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/derby");
            this.connection = ds.getConnection();
            prepareStatement();

//            //This creates a statement and executes it
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from users");
//            while (rs.next()) {
//                out.print(rs.getString("username") + "<br>");
//                out.println(rs.getString("password"));
//            }
        } catch (SQLException | NamingException e) {
            System.out.println(e.getMessage());
        }
    }

    public User getUser(String email, String password) {
        User user = null;
        try {
            this.getUser.setString(1, email);
            this.getUser.setString(2, password);
            try (ResultSet rs = this.getUser.executeQuery()) {
                if (rs.next()) {
                    System.out.println("[DBhandler]: User found!");
                    user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                    user.setResults(getResults(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    private ArrayList<Integer> getResults(int id) {
        ArrayList<Integer> dbresults = new ArrayList<>();
        try {
            this.getResults.setInt(1, id);
            try (ResultSet rs = this.getResults.executeQuery()) {
                while (rs.next()) {
                    dbresults.add(rs.getInt("score"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);

        }
        return dbresults;
    }

    public void updateDBResults(int user_id, int quiz_id, int score) {
        try {
            this.updateResult.setInt(1, score);
            this.updateResult.setInt(2, user_id);
            this.updateResult.setInt(3, quiz_id);
            this.updateResult.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retrieves all the available quizzes from the DB.
     *
     * @return An array list containing all available quizzes.
     */
    public ArrayList<Quiz> getQuizzes() {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        try (ResultSet rs = this.getAllQuizzes.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String subject = rs.getString("subject");
                // Create the quizz object and add it to the array list
                Quiz quiz = new Quiz(id, subject);
                ArrayList<Question> questions = getQuizQuestions(id);
                quiz.setQuestions(questions);
                quizzes.add(quiz);
            }
            System.out.println("[DBHandler] " + quizzes.size() + " quizzes added");
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quizzes;
    }

    private ArrayList<Question> getQuizQuestions(int questionId) {
        ArrayList<Question> questions = new ArrayList<>();
        try {
            this.getQuizQuestions.setInt(1, questionId);
            try (ResultSet rs = this.getQuizQuestions.executeQuery()) {
                while (rs.next()) {
                    //Get the data from the row
                    int id = rs.getInt("id");
                    String text = rs.getString("text");
                    String options = rs.getString("options");
                    String answer = rs.getString("answer");
                    // Parse the options and answer field
                    ArrayList<String> optionsList = parseDBQuestionString(options);
                    ArrayList<String> answerList = parseDBQuestionString(answer);
                    //Create the Question object
                    Question question = new Question(id, text, optionsList, answerList);
                    questions.add(question);
                }
                System.out.println("[DBHandler] Questions created");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return questions;
    }

    private ArrayList<String> parseDBQuestionString(String string) {
        ArrayList<String> list = new ArrayList<>();
        String[] parsedString = string.split("/");
        list.addAll(java.util.Arrays.asList(parsedString));
        return list;
    }

    private void prepareStatement() throws SQLException {
        this.getUser = this.connection.prepareStatement(
                "SELECT id, username, password FROM users WHERE username=? AND password=?"
        );
        this.getAllQuizzes = this.connection.prepareStatement(
                "SELECT id, subject FROM quizzes"
        );
        this.getQuizQuestions = this.connection.prepareStatement(
                "SELECT id, text, options, answer FROM selector INNER JOIN questions ON id=question_id WHERE quiz_id=?"
        );
        this.getResults = this.connection.prepareStatement(
                "SELECT quiz_id, score FROM results WHERE user_id=?"
        );
        this.updateResult = this.connection.prepareStatement(
                "UPDATE results SET score=? WHERE user_id=? AND quiz_id=?"
        );
    }
}
