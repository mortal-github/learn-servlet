package pers.mortal.learn.servlet.life;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Set;

@WebServlet(
        name = "PushBuilderExample",
        urlPatterns = "/life/push_builder_example",
        loadOnStartup = -1,
        initParams = {
                @WebInitParam(name = "path", value = "/")
        }
)
public class PushBuilderExample extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        //服务器推送
        String load = Optional.ofNullable(request.getParameter("load")).orElse("/advance/创建Servlet的流程.jpg");
        Optional.ofNullable(request.newPushBuilder())
                .ifPresent(pushBuilder -> pushBuilder.path(load).addHeader("Content-Type", "image/jpg").push());
    }
}