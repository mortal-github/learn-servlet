package pers.mortal.learn.servlet.async;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Queue;

@WebServlet(
        name = "asyncNumber2",
        urlPatterns = "/async/server-sent_event",
        asyncSupported = true
)
public class AsyncNumber2 extends HttpServlet {
    private Queue<AsyncContext> asyncContexts;

    @Override
    public void init() throws ServletException {
        asyncContexts = (Queue<AsyncContext>) this.getServletContext().getAttribute("asyncContexts2");
        assert null != asyncContexts;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/event-stream");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");

        AsyncContext ctx = request.startAsync();
        asyncContexts.add(ctx);
        ctx.setTimeout(30 * 1000);

        ctx.addListener(new AsyncListener(){
            @Override
            public void onComplete(AsyncEvent asyncEvent) throws IOException {asyncContexts.remove(ctx);}
            @Override
            public void onTimeout(AsyncEvent asyncEvent) throws IOException {asyncContexts.remove(ctx);}
            @Override
            public void onError(AsyncEvent asyncEvent) throws IOException {asyncContexts.remove(ctx);}
            @Override
            public void onStartAsync(AsyncEvent asyncEvent) throws IOException {}
        });
    }
}
