package pers.mortal.learn.servlet.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebListener
public class WebInitListener2 implements ServletContextListener {
    private Queue<AsyncContext> asyncContexts = new ConcurrentLinkedQueue<>();

    @Override
    public void contextInitialized(ServletContextEvent event){
           event.getServletContext().setAttribute("asyncContexts2", asyncContexts);

           new Thread(()->{
               while(true){
                   try{
                       Thread.sleep((int)(Math.random() * 5000));
                       response(Math.random() * 10);
                   }catch(Exception e){
                       throw new RuntimeException(e);
                   }
               }
           }).start();
    }

    private void response(double number){
        asyncContexts.forEach(ctx->{
            try{
                PrintWriter writer = ctx.getResponse().getWriter();
                writer.printf("data: %s\n\n", number);
                writer.flush();
            }catch(IOException e){
                throw new UncheckedIOException(e);
            }
        });
    }
}