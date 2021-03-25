package pers.mortal.learn.servlet.life;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Set;

@WebServlet(
        name = "ServletContextExample",
        urlPatterns = "/life/servlet_context_example",
        loadOnStartup = -1,
        initParams = {
                @WebInitParam(name = "path", value = "/")
        }
)
public class ServletContextExample extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();

        String path = Optional.ofNullable(request.getParameter("path")).orElse("/");
        String load = Optional.ofNullable(request.getParameter("load")).orElse("/advance/创建Servlet的流程.jpg");
        ServletContext context = this.getServletContext();

        Set<String> paths = context.getResourcePaths(path);
        InputStream inputStream = context.getResourceAsStream(load);

        paths.stream().forEach(i -> {
            try {
                outputStream.write((i + "<br>").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        while(inputStream.available() > 0) {
            outputStream.write(inputStream.read());
        }
    }
}
