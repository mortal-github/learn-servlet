package pers.mortal.learn.servlet.listener.servlet_context;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletRegistration;

@WebListener
public class ServletContextListenerExample implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event){
        ServletContext context = event.getServletContext();
        //ServletContext初始化参数，即应用程序初始化参数。
        //因为`@WebListener`没有设置应用程序初始参数的属性，
        //故只能在web.xml中用<context-param>的子元素<param-name>和<param-value>设置应用程序初始化参数的名字和值。
        String SERVLET_CONTEXT_INIT_PARAMETER = context.getInitParameter("SERVLET_CONTEXT_INIT_PARAMETER");
        context.setAttribute("SERVLET_CONTEXT_INIT_PARAMETER", SERVLET_CONTEXT_INIT_PARAMETER);

        //有些应用程序的设置，必须在Web应用程序初始时进行。
        //如改变HttpSession的一些Cookie设置，可以在web.xml方式中定义，
        //另一个方式是取得ServletContext后，使用getSessionCookieConfig()取得SessionCookieConfig进行设置，
        //不过这个动作必须在应用程序初始化时进行。
        SessionCookieConfig sessionCookieConfig = context.getSessionCookieConfig();
        sessionCookieConfig.setName("SessionIDName");

        //动态构造Servlet
        //ServletContext的addServlet新增Servlet，此时指定Servlet名字，构造函数，最后指定URI模式。
        ServletRegistration.Dynamic servlet = context.addServlet("dynamicServlet", new DynamicServlet(SERVLET_CONTEXT_INIT_PARAMETER));
        servlet.addMapping("/listener/servlet_context/dynamic_servlet");
        servlet.setLoadOnStartup(1);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event){
        event.getServletContext().removeAttribute("SERVLET_CONTEXT_INIT_PARAMETER");
    }
}
