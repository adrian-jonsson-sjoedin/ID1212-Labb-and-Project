<%-- 
    Document   : home
    Created on : Dec 2, 2023, 8:22:37 PM
    Author     : adrian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Quiz"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>
    <body>
        <h2>Choose a quiz to do.</h2>
        <% 
            ArrayList<Quiz> quizzes = (ArrayList<Quiz>) request.getSession().getAttribute("Quizzes");
            User user = (User) request.getSession().getAttribute("User");
            ArrayList<Integer> results = user.getResults();
            if (quizzes == null || quizzes.isEmpty()) {out.print("<div class=\"error\"><p>Something went wrong!</p></div>");}
            else {
                out.print("<form id=\"quizMenuForm\" action=\"Controller\" method=\"GET\">");
                for (Quiz quiz : quizzes) {
                    String subject = quiz.getSubject();
                    out.print("<input name=\"Subject\" class=\"subject\" type=\"submit\" value=" + subject +"><br>");
                    out.print("<h4>Current statistics</h4>");
                    for (int i = 0; i < results.size(); i++) {
                    out.print("<p>" + results.get(i) + "/" + quiz.getMaxScore() + "</p>");
                }
            }
                out.print("</form><br>");
                
            }
        %>
    </body>
</html>
