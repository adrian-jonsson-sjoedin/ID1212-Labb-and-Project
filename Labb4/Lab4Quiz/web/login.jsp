<%-- 
    Document   : login
    Created on : Dec 2, 2023, 6:43:22 PM
    Author     : adrian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <style>
            .error {color: red;}
        </style>
    </head>
    <body>
        <h1>ID1212 Lab 4.</h1>
        <h2>Quiz</h2>
        <%  
            String status = (String) request.getSession().getAttribute("SessionStatus");
            if (status != null && status.equals("NotFound")) {
                out.print("<h4 class=\"error\">No user found matching username and password!</h4>");
            }
            else if (status != null && status.equals("Error")) {
                out.print("<div class=\"error\"><p>Something went wrong!</p></div>");
            }
        %>
        <form id="LoginForm" action="Controller" method="GET">
            <input name="Username" type="text" placeholder="Enter email" required/>
            <input name="Password" type="password" placeholder="Enter password" required/>
            <input name="Login" type="submit" value="Login"/>
        </form>
    </body>
</html>
