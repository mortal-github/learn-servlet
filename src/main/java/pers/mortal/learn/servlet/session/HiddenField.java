package pers.mortal.learn.servlet.session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/session/hidden-filed")
public class HiddenField  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println("<!DOCTYPE html>");
        writer.println("<html><head><meta charset=\"UTF-8\"></head><body>");
        writer.println("<form action=\"hidden-filed\" method=\"post\">");
        //根据请求参数选择显示页面
        String page = request.getParameter("page");
        if("page1".equals(page)) {
            page1(writer);
        }else if("page2".equals(page)){
            page2(request, writer);
        }else if("finish".equals(page)){
            page3(request, writer);
        }
        writer.println("</from></body></html>");
    }

    private void page1(PrintWriter writer){
        writer.println("问题一：<input type=\"text\" name=\"p1q1\"<br>");
        writer.println("问题二：<input type=\"text\" name=\"p1q2\"<br>");
        writer.println("<input type=\"submit\" name=\"page\" value=\"page2\"<br>");
    }
    private void page2(HttpServletRequest request, PrintWriter writer){
        String p1q1 = request.getParameter("p1q1");
        String p1q2 = request.getParameter("p1q2");
        writer.println("问题三：<input type=\"text\" name=\"p2q1\"<br>");
        //作为响应的隐藏域，让浏览器再下次请求的时候发送内容。
        writer.printf("<input type=\"hidden\" name=\"p1q1\" value=\"%s\">\n", p1q1);
        writer.printf("<input type=\"hidden\" name=\"p1q2\" value=\"%s\">\n", p1q2);
        writer.println("<input type=\"submit\" name=\"page\" value=\"finish\"<br>");
    }
    private void page3(HttpServletRequest request, PrintWriter writer){
        writer.println(request.getParameter("p1q1") + "<br>");
        writer.println(request.getParameter("p1q2") + "<br>");
        writer.println(request.getParameter("p2q1") + "<br>");
    }

}