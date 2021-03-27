package pers.mortal.learn.servlet.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(
        name = "AsyncNumber",
        urlPatterns = "/async/long_polling",
        asyncSupported = true
)
public class AsyncNumber extends HttpServlet {
    private List<AsyncContext> asyncContexts;

    @Override
    public void init()throws ServletException {
        this.asyncContexts = (List<AsyncContext>) this.getServletContext().getAttribute("asyncContexts");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        synchronized(asyncContexts){
            asyncContexts.add(request.startAsync());
        }
    }
}
