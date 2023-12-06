/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author adrian
 */
public class Question {
    private int id;
    private String text;
    private ArrayList<String> options;
    private ArrayList<String> answer;
    private int maxScore;

    public Question(int id, String text, ArrayList<String> options, ArrayList<String> answer) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.answer = answer;
        calculateMaxScore();
    }
    private void calculateMaxScore(){
        int max=0;
        for(int i = 0; i<answer.size(); i++){
            if(answer.get(i).equals("1")){
                max++;
            }
        }
        this.maxScore = max;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

}
