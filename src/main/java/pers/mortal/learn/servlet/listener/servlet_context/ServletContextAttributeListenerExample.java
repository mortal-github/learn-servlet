package pers.mortal.learn.servlet.listener.servlet_context;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContextAttributeListenerExample implements ServletContextAttributeListener {
    @Override
    public void attributeAdded(ServletContextAttributeEvent event){
        String name = event.getName();
        Object value = event.getValue();
        System.out.println("added   : " + name + " = " + value);
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event){
        String name = event.getName();
        Object value = event.getValue();
        System.out.println("removed  : " + name + " = " + value);
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event){
        String name = event.getName();
        Object value = event.getValue();
        System.out.println("replaced : " + name + " = " + value + " ==> " + event.getServletContext().getAttribute(name));
    }
}