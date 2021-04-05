package pers.mortal.learn.servlet.security;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "user",
        urlPatterns = "/security/user",
        loadOnStartup = -1
)
public class User extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.authenticate(response)){//如果authenticate()结果是false，表示用户未登录，在service()完成后，会自动转发至登录窗体(login.html)。
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("必须验证用户才可以看到的资料");
            writer.println("<a href='logout'>注销</a>");
        }

    }
}
