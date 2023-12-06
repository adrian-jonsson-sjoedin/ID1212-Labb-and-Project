/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a quiz
 *
 * @author adrian
 */
public class Quiz implements Serializable {

    private final int id;
    private final String subject;
    private ArrayList<Question> questions;
    private int maxScore;

    public Quiz(int id, String subject) {
        this.id = id;
        this.subject = subject;
        
    }

    public int getMaxScore() {
        return maxScore;
    }

    private void calculateMaxScore() {
        int max = 0;
        for (Question question : questions) {
            max += question.getMaxScore();
        }
        this.maxScore = max;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
        calculateMaxScore();
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

}
