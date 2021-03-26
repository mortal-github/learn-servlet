## Servlet事件、监听器 

**事件监听器**：
- **管理生命周期**： Web容器管理Servlet/JSP的对象生命周期。  
- **监听对象的时机点**：Web容器提供监听器可以监听下述对象在**对象生成**、**销毁**、**属性变化**的时机点，让用户可以在对应的时机点做些处理。  
    - **`ServletContext`**:
    - **`HttpSession`**:
    - **`ServletRequest`**:
- **使用监听器**：
    1. 实现监听器接口。
    2. 然后配置监听器类（有些监听器不需要配置）。 

### `ServletContext`应用程序监听器  
- **`ServletContextListener`**是”生命周期监听器“接口：监听生命周期，在Web应用程序**已经初始化**或即将**结束销毁**。   
- **`ServletContextAttributeListener`**是”属性变化监听器“接口：：监听`ServletContext`属性**被设置（添加）**、**移除**或**替换**。  

**`ServletContext`相关监听器接口API**：
```java
package javax.servlet;
import java.util.EventListener;
public interface ServletContextListener extends EventListener{
    default void contextInitialized(ServletContextEvent event){}    //在Web应用程序初始化后调用。 可以实现应用程序资源的准备工作。
    default void contextDestroyed(ServletContextEvent event){}      //在Web应用程序结束前调用。   可以实现释放应用程序资源的工作。
}
public class ServletContextEvent{
    public ServletContext getServletContext(){}     //获取ServletContext。
}

public interface ServletContextAttributeListener extends EventListener{
    default void attributedAdded(ServletContextAttributeEvent event);       //在ServletContext添加属性时调用。
    default void attributedRemoved(ServletContextAttributeEvent event);     //在ServletContext移除属性时调用。
    default void attributedReplaced(ServletContextAttributeEvent event);    //在ServletContext替换属性时调用。
}
public class ServletContextAttributeEvent{
    public String getName();                    //获取属性的名称。
    public Object getValue();                   //获取属性的值。
    public ServletContext getServletContext();  //获取ServletContext
}
```

**配置监听器**：
- 配置`ServletContextListener`监听器：
    - **`@WebListener`注解**：在实现`ServletContextListener`接口的类上使用`@WebListener`注解，容器就会在启动时自动加载并运行相关方法。    
    - **`web.xml`文件XML配置**：使用**`<listener>`**元素的子元素**`<listener-class>`**指出**监听器实现类的全限定名**。  
- 配置`ServletContextAttributeListener`监听器：
    - **`@WebListener`注解**：在实现`ServletContextListener`接口的类上使用`@WebListener`注解，容器就会在启动时自动加载并运行相关方法。    
    - **`web.xml`文件XML配置**：使用**`<listener>`**元素的子元素**`<listener-class>`**指出**监听器实现类的全限定名**。  

**应用程序初始化参数、设置变更、动态Servlet**：
- `ServletContext`初始化参数：可以为应用程序定义初始化参数，作用于Servlet初始化参数类似，做为常数配置。
    - **注解无法配置**：因为`@WebListener`没有设置应用程序初始参数的属性。  
    - **XML配置**：故只能在`web.xml`中用`<context-param>`的子元素`<param-name>`和`<param-value>`设置应用程序初始化参数的名字和值。  
- 应用程序设置变更：有些应用程序的设置，必须在Web应用程序初始时进行。  
    - `SessionCookieConfig`：如改变`HttpSession`的一些`Cookie`设置，可以在web.xml方式中定义。  
    - 应用程序初始化时配置：另一个方式是**在应用程序初始化时**取得`ServletContext`后，使用`getSessionCookieConfig()`取得`SessionCookieConfig`进行设置.  
- **动态设置`Servlet`**:
    - `addServlet(String name, Servlet servlet)`方法:`ServletContext`的`addServlet()`方法新增`Servlet`，指定名字和Servlet实例（自行构造）。 
    - `ServletRegistration.Dynamic`类实例：`addServlet`方法返回`ServletRegistration.Dynamic`代表动态Servlet。  
    - `addMapping(String url)`方法：`ServletRegistration.Dynamic`实例的`addMapping`方法最后指定URI模式。  
    - `setLoadOnStartup(int)`方法：`ServletRegistration.Dynamic`实例的`setLoadOnStartup(int)`方法设置`load-on-startup`配置属性。

**示例**：
```java
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
        //ServletContext的addServlet新增Servlet，此时指定Servlet名字，构造实例（此时可以使用自定义构造器），最后指定URI模式。
        ServletRegistration.Dynamic servlet = context.addServlet("dynamicServlet", new DynamicServlet(SERVLET_CONTEXT_INIT_PARAMETER));
        servlet.addMapping("/listener/servlet_context/dynamic_servlet");
        servlet.setLoadOnStartup(1);
    }
    @Override
    public void contextDestroyed(ServletContextEvent event){
        event.getServletContext().removeAttribute("SERVLET_CONTEXT_INIT_PARAMETER");
    }

}

public class DynamicServlet extends HttpServlet {
    private final String SERVLET_CONTEXT_INIT_PARAMETER;                          //final值域。
    public DynamicServlet(String SERVLET_CONTEXT_INIT_PARAMETER){                 //通过动态构造Servlet就能调用构造器来实例化Servlet，进而才能声明为final域。
        this.SERVLET_CONTEXT_INIT_PARAMETER = SERVLET_CONTEXT_INIT_PARAMETER;   //动态构造Servlet不需要配置Servlet。
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println("SERVLET_CONTEXT_INIT_PARAMETER = " + this.SERVLET_CONTEXT_INIT_PARAMETER);
    }
}
```
```java
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
```
```xml
<web-app>
    <!--应用程序初始化参数-->
    <context-param>
        <param-name>AVATAR</param-name>
        <param-value>/avatar</param-value>
    </context-param>
    <!--监听器-->
    <listener>
        <listener-class>pers.mortal.learn.servlet.listener.servlet_context.ServletContextAttributeListenerExample</listener-class>
    </listener>
    <listener>
        <listener-class>pers.mortal.learn.servlet.listener.servlet_context.ServletContextListenerExample</listener-class>
    </listener>
</web-app>
```

### `HttpSession`事件、监听器

**`HttpSession`监听器**：有5个。
- **`HttpSessionListener`**是“生命周期监听器”:  
- **`HttpSessionAttributeListener`**是“属性变化监听器”: 
- **`HttpSessionBingListener`**是“对象绑定监听器”: 不需要注释或web.xml配置，当实现此接口的属性对象比加入`HttpSession`或从中移除时，就会调用对应的方法。  
- **`HttpSessionActivationListener`是“对象迁移监听器”**: 
- **`HttpSessionIdListener`**: Servlet3.1新增，监听`SessionID`的变化。 
> 对象迁移监听器：
> 大多数请况都用不到这种监听器。 
> 在分布式环境中，应用程序的对象可能分散在多个JVM中。 
> 当HttpSession要从一个JVM迁移至另一个JVM时，必须现在原本的JVM上序列化所有属性对象，
> 在这之前若属性对象实现`HttpSessionActivationListener`，就会调用`sessionWillPassivate()`方法，
> 而`HttpSession`迁移至另一个JVM后，就会对所有属性对象作反序列化，此时会调用`sessionDidActivate()`方法。 
> 用途：
> 要可以序列化的对象必须实现`Serializable`接口。
> 如果`HttpSession`属性对象中有些类成员无法做序列化，可以在`sessionWillPassivate()`方法中做些替代处理来保存该成员状态。
> 而在`sessionDidActivate()`方法中做些恢复成员状态的动作。  

**`HttpSession`相关监听器接口API**:
```java
package javax.servlet.http;
import java.awt.*;import java.util.EventListener;

public interface HttpSessionListener extends EventListener{
    default void sessionCreated(HttpSessionEvent event){}       //在HttpSession对象初始化后调用。
    default void sessionDestroyed(HttpSessionEvent event){}     //在HttpSession对象结束前调用。
}
public class HttpSessionEvent extends Event{
    public HttpSession getSession(){}   //取得HttpSession对象。
}

public interface HttpSessionAttributeListener extends EventListener{
    default void attributeAdded(HttpSessionBindingEvent event){}    //在增加HttpSession属性时调用。
    default void attributeRemoved(HttpSessionBindingEvent event){}  //在移除HttpSession属性时调用。
    default void attributeReplaced(HttpSessionBindingEvent event){} //在替换HttpSession属性时调用。
}
public class HttpSessionBindingEvent extends Event{
    public String getName();    //获得设置或移除时的属性名。
    public String getValue();   //取得属性设置或移除时的对象。
}

public interface HttpSessionBindingListener extends EventListener{
    default void valueBound(HttpSessionBindingEvent event){}    //实现该接口的对象被加入为HttpSession属性时调用。
    default void valueUnbound(HttpSessionBindingEvent event){}  //实现该接口的对象作为HttpSession属性被移除时调用。
}

public interface HttpSessionActivateListener extends EventListener{
    default void sessionWillPassivate(HttpSessionEvent event){} //实现该接口的对象作为HttpSession属性在HttpSession迁移JVM前调用。
    default void sessionDidActivate(HttpSessionEvent event){}   //实现该接口的对象作为HttpSession属性在HttpSession迁移JVM后调用。
}

public interface HttpSessionIdListener extends EventListener{
    default void sessionIdChanged(HttpSessionEvent event, String oldSessionId){}  //当HttpSession的SessionID发生变化时调用。 
}
```

**配置`HttpSession`监听器**：
- `HttpSessionListener`：使用**`@WebListener`**注解监听器实现类，或则`web.xml`中使用**`<listener>`的`<listener-class>`**指定实现类的全限定名。  
- `HttpSessionAttributeListener`：使用`@WebListener`注解监听器实现类，或则`web.xml`中使用**`<listener>`的`<listener-class>`**指定实现类的全限定名。  
- `HttpSessionBingListener`：为需要监听绑定的**属性对象实现该接口即可**，不需要注解或XML配置。  
- `HttpSessionActivationListener`：为需要在`HttpSession`迁移前后做处理的**属性对象实现该接口即可**，不需要注解或XML配置。
- `HttpSessionIdListener`：使用**`@WebListener`**注解监听器实现类，或则`web.xml`中使用**`<listener>`的`<listener-class>`**指定实现类的全限定名。  

**示例**：
```java
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
```

### `HttpServletRequest`事件、监听器 
- `ServletRequestListener`是“生命周期监听器”:
- `ServletRequestAttributeListener`是“属性变化监听器”:
- `AsyncListener`:Servlet3.0增加，在异步处理再讲解。
- `ReadListener`: Servlet3.1增加，再异步处理再讲解。

**`HttpServletRequest`相关监听器API**:
```java
package javax.servlet;
import java.util.EventListener;

public interface ServletRequestListener extends EventListener{
    default requestInitialized(ServletRequestEvent event){}     //请求对象初始化后调用。
    default requestDestroyed(ServletRequestevent event){}       //请求对象销毁前调用。
}
public class ServletRequestEvent extends Event{
    ServletRequest getServletRequest(){}
}

public interface ServletRequestAttributeListener extends EventListener{
    default void attributeAdded(ServletRequestAttributeEvent event){}       //请求属性被添加时调用。
    default void attributeRemoved(ServletRequestAttributeEvent event){}     //请求属性被移除时调用。
    default vodi attributeReplaced(ServletRequestAttributeEvent event){}    //请求属性被替换时调用。
}
public class ServletRequestAttributeEvent extends Event{
    public String getName(){}   //获取设置或移除时的属性名
    public Object getValue(){}  //获取设置或移除属性时的对象。
}
```

**配置`HttpServletRequest`相关监听器API**:
- `ServletRequestListener`:在实现`ServletContextListener`接口的类上使用`@WebListener`注解，容器就会在启动时自动加载并运行相关方法。    
- `ServletRequestAttributeListener`:在实现`ServletContextListener`接口的类上使用`@WebListener`注解，容器就会在启动时自动加载并运行相关方法。    