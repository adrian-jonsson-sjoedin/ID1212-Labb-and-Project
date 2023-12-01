<%-- 
    Document   : view
    Created on : Dec 1, 2023, 8:30:54 PM
    Author     : adrian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.GameSession" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Guess the Number!</title>
    </head>
    <body>
        <h1>ID1212 Lab 4.</h1>
        <h2>Guess the number game.</h2>
        <div id="messageToPlayer">
            <%--  Display a message conditionally to the player based on game session state--%>
            <%
              if(request.getSession().getAttribute("GameSession") == null){
                out.print("Welcome to the Number Guess Game. Guess a number between 1 and 100.");
              }else {
                GameSession game = (GameSession) request.getSession().getAttribute("GameSession");
                String answer = (String) request.getSession().getAttribute("Answer");
                int numbOfGuesses = game.getNumbOfGuesses();
                switch (answer) {
                case "NO GUESS": 
                    out.print("Welcome to the Number Guess Game. Guess a number between 1 and 100.");
                    break;
                case "NOT_IN_RANGE":
                    out.print("You have to guess on an integer between 1 and 100!");
                    break;
                case "LOWER":
                    out.print("Nope, guess lower! You have made " + numbOfGuesses + " guess(es).");
                    break;
                case "HIGHER":
                    out.print("Nope, guess higher! You have made " + numbOfGuesses + " guess(es).");
                    break;
                case "CORRECT":
                    out.print("You made it!!! You have made " + numbOfGuesses + " guess(es).");
                    break;
                default:
                    out.print("Something went wrong...");
                    break;
                }
               }
            %>    
        </div>
        <%-- The form where the player can enter their guess --%>
        <form id="gameForm" action="Controller" method="GET">
            <input type="number" name="guessAsString" placeholder="Enter a number..." required>
            <input type="submit" value="Guess!">
        </form>    
    </body>
</html>
