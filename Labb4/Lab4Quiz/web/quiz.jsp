<%-- 
    Document   : quiz
    Created on : Dec 2, 2023, 10:06:14 PM
    Author     : adrian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.Quiz"%>
<%@page import="model.Question"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quiz</title>
    </head>
    <body>
        <%
            Quiz quiz = (Quiz) request.getSession().getAttribute("CurrentQuiz");
            ArrayList<Question> questions = quiz.getQuestions();
            out.print("<h2>" +quiz.getSubject() +" Quiz </h2>");
            out.print("<form id='quiz' action=\"Controller\" method=\"POST\">");
            int counter = 1;
            for(Question question: questions){
                out.print("<h4> Question " + counter + "</h4>");
                out.print("<p>" + question.getText() + "</p>");
                int i = 1;
                for(String option : question.getOptions()){
                    out.print("<input name=\"Option"+counter +"\" type=\"checkbox\" value=\"" + option + "\" /> " + option + "<br>");
            i++;    
            }
                counter++;
            }
            out.print("<input name=\"SubmitedQuiz\" type=\"submit\" value=\"Submit Quiz\"/>");
            out.print("</form>");
        %>
    </body>
</html>
