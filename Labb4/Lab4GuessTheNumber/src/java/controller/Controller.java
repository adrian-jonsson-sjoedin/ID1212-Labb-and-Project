/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GameSession;

/**
 *Acts as a controller for the application. Handles incoming HTTP GET requests and 
 * response 
 * @author adrian
 */
public class Controller extends HttpServlet {

    /**
     * Processes requests HTTP <code>GET</code> 
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
        /*
        This line obtains the session associated with the current request. 
        If a session doesn't exist, a new one is created*/
        HttpSession session = request.getSession(true);
        // Create an instance of the GameSession model
        GameSession game = new GameSession();
        /*
        This block checks if there's an existing game session in the session 
        attributes, and if the previous answer is not "CORRECT." 
        If these conditions are met, it retrieves the existing game session 
        from the session, i.e., we have an existing game which we retrieve.
        */
        if ((session.getAttribute("GameSession") != null) && !(session.getAttribute("Answer").equals("CORRECT"))) {
            game = (GameSession) session.getAttribute("GameSession");
        }
        
        //Performe the game logic in the model
        String userGuess = request.getParameter("guessAsString");
        String answer = game.guess(userGuess);
        
        //Update the current session for the view and send the response
        session.setAttribute("Answer", answer);
        session.setAttribute("GameSession", game);
        RequestDispatcher rd = request.getRequestDispatcher("view.jsp");
        rd.forward(request, response);

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
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }

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
