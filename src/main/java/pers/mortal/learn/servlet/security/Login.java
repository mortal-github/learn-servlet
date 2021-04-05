package pers.mortal.learn.servlet.security;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "login",
        urlPatterns =  "/security/login",
        loadOnStartup = -1
)
public class Login extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String user = request.getParameter("user");
        String passwd = request.getParameter("passwd");
        try{
            request.login(user, passwd);
            response.sendRedirect("user");
        }catch(ServletException e){
            response.sendRedirect("login.html");
        }
    }
}
