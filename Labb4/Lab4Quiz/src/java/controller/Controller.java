/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import integration.DBHandler;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.Question;
import model.Quiz;
import model.User;

/**
 *
 * @author adrian
 */
public class Controller extends HttpServlet {

    private DBHandler database;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.database = new DBHandler();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        ArrayList<Quiz> quizzes = (ArrayList<Quiz>) session.getAttribute("Quizzes");
        //generate the quizzes if not already done
        if (quizzes == null || quizzes.isEmpty()) {
            quizzes = database.getQuizzes();
            session.setAttribute("Quizzes", quizzes);
        }

        //Login
        String username = request.getParameter("Username");
        String password = request.getParameter("Password");
        if (username != null && password != null) {
            User user = this.database.getUser(username, password);
            if (user == null) {//this means we did not found a matching user in the DB
                session.setAttribute("SessionStatus", "NotFound");
                response.sendRedirect("login.jsp");
                return;
            } else {
                System.out.println("[Controller] User " + user.getUsername() + "has logged in.");
                session.setAttribute("User", user);
                session.setAttribute("SessionStatus", "Login");
                RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
                rd.forward(request, response);
                return;
            }
        }
        //Navigate to the choosen quiz
        String quizSubject = request.getParameter("Subject");
        if (quizSubject != null) {
            //We loop through the quizzes to find the one whose subject is the 
            //same as the choosen one. We then set that quiz as the current quiz
            for (Quiz quiz : quizzes) {
                if (quiz.getSubject().equals(quizSubject)) {
                    session.setAttribute("CurrentQuiz", quiz);
                    RequestDispatcher rd = request.getRequestDispatcher("quiz.jsp");
                    rd.forward(request, response);
                    return;
                }
            }
        }

        //Now we need to handle the quiz submit
        String submitedQuiz = request.getParameter("SubmitedQuiz");
        if (submitedQuiz != null) {
            /*
            First we see wich options where correct for each question by looking at
            the results and options ArrayList. And then we can look at the results
            from the form and see if they match.
             */
            Quiz currentQuiz = (Quiz) session.getAttribute("CurrentQuiz");
            ArrayList<Question> questions = currentQuiz.getQuestions();
            Integer score = 0;
            for (int i = 0; i < questions.size(); i++) {
                //get the options and answer for each question and create a list
                //containing the correct options. These are all for just one line
                ArrayList<String> options = questions.get(i).getOptions();
                ArrayList<String> answers = questions.get(i).getAnswer();
                ArrayList<String> correctOptionsForQuestion = new ArrayList<>();
                for (int j = 0; j < answers.size(); j++) {
                    if (answers.get(j).equals("1")) {
                        correctOptionsForQuestion.add(options.get(j));
                    }
                }
                ArrayList<String> lineFormAnswers = retrieveFormAnswerPerQuestion(request, i + 1);
                //debugging
//                System.out.println("Printing the correct options for the question:");
//                for (int j = 0; j < correctOptionsForQuestion.size(); j++) {
//                    System.out.println(correctOptionsForQuestion.get(j));
//                }
//                System.out.println("Printing the form answers for the question:");
//                for (int j = 0; j < lineFormAnswers.size(); j++) {
//                    System.out.println(lineFormAnswers.get(j));
//                }
                score += getMatchingElementsSize(lineFormAnswers, correctOptionsForQuestion);
            }
            System.out.println("Final score was " + score);

            //Update session and database with the new score
            User user = (User) session.getAttribute("User");
            ArrayList<Integer> newResults = user.getResults();
            newResults.add(currentQuiz.getId() - 1, score);
            user.setResults(newResults);
            System.out.println("Result size is " + user.getResults().size());
            database.updateDBResults(user.getId(), currentQuiz.getId(), score);
            session.setAttribute("SubmitedQuiz", null);
//            session.setAttribute("CurrentQuiz", null);

            RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
            rd.forward(request, response);
        }
    }

    private int getMatchingElementsSize(ArrayList<String> formAnswers, ArrayList<String> correctOptions) {
        ArrayList<String> commonElements = new ArrayList<>(correctOptions);
        // Retain only the elements that are common between formAnswers and correctOptions
        commonElements.retainAll(formAnswers);
        // Count the number of common elements
        int count = commonElements.size();
//        System.out.println("We got " + count + " correct for this question.");
        return count;
    }

    private ArrayList<String> retrieveFormAnswerPerQuestion(HttpServletRequest request, int index) {
        ArrayList<String> answers = new ArrayList<>();
        int counter = 1;
        while (request.getParameter("Option" + counter) != null) {
            if (counter == index) {
                String[] selectedOptions = request.getParameterValues("Option" + counter);
                if (selectedOptions != null) {
                    for (String option : selectedOptions) {
                        System.out.println("[retrieveFormAnswers] Option" + counter + "=" + option);
                        answers.add(option);
                    }
                }
//                counter++;
                break; // Exit the loop once the desired option group is processed
            }
            counter++;
        }
        return answers;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
