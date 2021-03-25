package pers.mortal.learn.servlet.life;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "ServletInitParameter",
        urlPatterns = "/life/servlet_init_parameter",
        loadOnStartup = -1,
        initParams = {
                @WebInitParam(name = "姓名", value = "钟景文"),
                @WebInitParam(name = "方式", value = "注解"),
                @WebInitParam(name = "作用", value = "默认常数")
        }
)
public class ServletConfigExample extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();

        ServletConfig config = this.getServletConfig();
        String name = config.getInitParameter("姓名");
        String way = config.getInitParameter("方式");
        String function = config.getInitParameter("作用");

        writer.println("姓名 = " + name + "<br>");
        writer.println("方式 = " + way + "<br>");
        writer.println("作用 = " + function + "<br>");
    }
}
