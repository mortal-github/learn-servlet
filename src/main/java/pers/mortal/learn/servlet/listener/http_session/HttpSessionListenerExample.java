package pers.mortal.learn.servlet.listener.http_session;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class HttpSessionListenerExample implements HttpSessionListener {
    public static int counter = 0;
    @Override
    public void sessionCreated(HttpSessionEvent event){
        HttpSessionListenerExample.counter++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event){
        HttpSessionListenerExample.counter--;
    }
}
