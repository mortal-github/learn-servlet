package pers.mortal.learn.servlet.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class WebInitListener implements ServletContextListener {

    private List<AsyncContext> asyncContexts= new ArrayList<>();

    @Override
    public void contextInitialized(ServletContextEvent event){
        event.getServletContext().setAttribute("asyncContexts", asyncContexts);

        new Thread(()->{
            while(true){
                try{
                    Thread.sleep((int)(Math.random() * 5000));
                    response(Math.random() * 10);
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        ).start();
    }

    private void response(double number){

        synchronized(asyncContexts){
            asyncContexts.forEach(ctx->{
                try{
                    ctx.getResponse().getWriter().println(number);
                    ctx.complete();
                }catch(IOException e){
                    throw new UncheckedIOException(e);
                }
            });
            asyncContexts.clear();
        }
    }
}
