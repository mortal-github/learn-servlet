package pers.mortal.learn.servlet.session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.stream.IntStream;

@WebServlet("/session/uri-rewrite")
public class URIRewriter extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println("<!DOCTYPE html>");
        writer.println("<html><head><meta charset=\"UTF-8\"></head><body>");

        results(request, writer);
        pages(request, writer);

        writer.println("</body></html>");
    }

    private void results(HttpServletRequest request, PrintWriter writer){
        String page = Optional.ofNullable(request.getParameter("page")).orElse("1");
        final int select = Integer.parseInt(page) - 1;

        writer.println("<ul>");
        IntStream.rangeClosed(1, 10).forEach(i -> writer.printf("<li>搜索结果 %d</li>\n", i + 10*select));
        writer.println("</ul>");
    }
    private void pages(HttpServletRequest request, PrintWriter writer){
        String page = Optional.ofNullable(request.getParameter("page")).orElse("1");
        int select = Integer.parseInt(page);

        IntStream.rangeClosed(1, 10).forEach(i ->{
            if(i == select){
                writer.println(i);
            }else{
                writer.printf("<a href=\"uri-rewrite?page=%d\">%d</a>\n", i, i);
            }
        });
    }
}
