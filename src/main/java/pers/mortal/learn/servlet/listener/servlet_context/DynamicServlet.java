package pers.mortal.learn.servlet.listener.servlet_context;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DynamicServlet extends HttpServlet {
    private final String SERVLET_CONTEXT_INIT_PARAMETER;                          //final值域。
    public DynamicServlet(String SERVLET_CONTEXT_INIT_PARAMETER){                 //通过动态构造Servlet就能调用构造器来实例化Servlet，进而才能声明为final域。
        this.SERVLET_CONTEXT_INIT_PARAMETER = SERVLET_CONTEXT_INIT_PARAMETER;   //动态构造Servlet不需要配置Servlet。
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println("SERVLET_CONTEXT_INIT_PARAMETER = " + this.SERVLET_CONTEXT_INIT_PARAMETER);

    }
}
