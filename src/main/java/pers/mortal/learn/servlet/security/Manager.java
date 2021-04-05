package pers.mortal.learn.servlet.security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.Writer;

@WebServlet(
        name = "manager",
        urlPatterns = "/manager/*",
        loadOnStartup = -1
)
public class Manager extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doMethod(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doMethod(request, response);
    }

    @Override
    public void doHead(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doMethod(request, response);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doMethod(request, response);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doMethod(request, response);
    }

    @Override
    public void doTrace(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doMethod(request, response);
    }

    @Override
    public void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doMethod(request, response);
    }

    private void doMethod(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpServletMapping mapping = request.getHttpServletMapping();
        Writer writer = response.getWriter();

        String pattern = mapping.getPattern();
        String value = mapping.getMatchValue();
        MappingMatch match = mapping.getMappingMatch();

        writer.write("pattern       = " + pattern + "<br>\n");
        writer.write("match_value   = " + value + "<br>\n");
        writer.write("mapping match = " + match + "<br>\n");
    }
}
