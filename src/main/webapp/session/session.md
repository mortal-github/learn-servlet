## 会话管理  

- **无状态协议**： HTTP无状态协议不会记得请求之间的关系，也即是说服务器每次处理一个请求都会知道这次请求与下一次请求的关系。  
- **会话管理**：会话管理提供了记得多次请求之间的关系的方式。   
- **会话管理的方法**： 有3种基本的会话管理方式，基于此，Servlet容器提供了更强大便捷的`Session`类管理会话。
    - **隐藏域**：本次请求的结果作为**响应的隐藏域**。浏览器下次请求的时候再把隐藏域的内容**主动发送给服务器**。 
    - **`URI`重写**：本次请求的响应中，将某些相关信息以**超链接**的方式发送给浏览器，超链接中**包含请求参数**。
    - **`Cookie`**：`Cookie`是在浏览器**储存信息**的一种方式。  
    - **`HttpSession`类**：

### 隐藏域
- **简单的状态管理**：关闭网页后，显然会遗失信息，故仅适合用于简单的会话管理。 
- **隐秘性低**：显然，查看网页源代码可以看到隐藏域的值，不适合用于隐秘性高的数据。  
```java
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
        writer.println("<html><head><meta charset=\"UTF-8\"></head>");
        writer.println("<body>");
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
```

### URI重写 
- 使用`GET`请求参数：URL重写其实就是`GET`请求参数的应用。 
- 保留信息少：URI重写是在超链接之后附加信息，必须以GET方式发送请求。而GET请求参数长度有限，故不适合大量信息保留。
- 辅助会话管理：URI重写使用在一些简单的浏览器信息保留，或者是辅组会话管理。  
```java
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

```

### 使用`Cookie`
- **`set-cookie`响应标头**：Web应用程序可以响应浏览器`set-cookie`标头，浏览器收到这个**标头与值（值是键/值对）**后，将它以文件的形式储存在计算机上。 
- **`Cookie`请求标头**： 若`Cookie`仍在有效期内，浏览器会自动使用**`Cookie`标头**发送给Web应用程序。  
- **存活期限**：可以设定给`Cookie`一个存活期限，默认为储存到浏览器关闭。  
- **安全性**： 
    - `Cookie`类的`setSecure(boolean flag)`方法：设置为true就只会在连机有**加密（HTTPS）的情况下传送`Cookie`**。   
    - `Cookie`类的`setHttpOnly(boolean isHttpOnly)`方法：Servlet3.0新增，在**`set-Cookie`标头上附加`HttpOnley`属性**，在浏览器支持的情况下，这个`Cookie`**不会被`JavaScript`读取**。   
    - `Cookie`类的`isHttpOnly()`方法：返回这个`Cookie`是否添加`HttpOnly`的判断。  
> 如果关闭浏览器之后再次打开并连接Web应用程序，若`Cookie`仍在有效期内，浏览器会自动使用`Cookie`标头发送给Web应用程序。  
> 浏览器被预期能为每个网站储存20个Cookie，总共可以储存300个Cookie，而每个Cookie的大小不超过4KB（具体因浏览器而异）。
> 因此Cookie上可储存的信息也是有限的。                                        
```java
public class Cookie{
    public Cookie(String name, String value){}      //创建一个Cookie，传入名称与值。
    public void setMaxAge(long second){}            //设置存活期限，单位是秒。默认关闭浏览器之后就失效。
    public String getName(){}                       //获取Cookie名称。
    public String getValue(){}                      //获取Cookie的值。
    public void setSecure(boolean flag){}           //设置为true就只会在连机有加密（HTTPS）的情况下传送Cookie。 
    public void setHttpOnly(boolean isHttpOnly){}   //Servlet3.0新增，在set-Cookie标头上附加HttpOnly属性，在浏览器支持的情况下，这个Cookie不会被JavaScript读取。 
    public boolean isHttpOnly(){}
}
public interface HttpServletResponse extends ServletResponse{
    void addCookie(Cookie cookie);      //添加一个Cookie。
}
public interface HttpServletRequest extends ServletRequest{
    Cookie[] getCookies();              //获取所有的Cookie。  
}
```

### `HttpSession`
- **获取`HttpSession`**：`HttpServletRequest`的`getSession()`方法获此**此次请求所属的会话（通过`SesssionID`判断请求属于哪个会话）**的`HttpSession`对象。  
- **与`Cookie`的区别**：`Cookie`是储存信息在浏览器，而`HttpSession`是**储存信息在容器**。  
    > 故`HttpSession`会话管理能储存更多信息，且减少了每次请求都要解析信息的工作。
- **默认存活期限**：默认在关闭浏览器之前，取得的`HttpSession`对象都是相同的实例。   
- **会话中使`HttpSession`失效**：
    - **`HttpSession`类的`invalidate()`方法**：例如实现注销机制，显然需要关闭会话，即使得此次会话的`HttpSession`失效。  
    - **`HttpServletRequest`类的`changeSessionId()`方法**：Servlet3.1增加，基于Web安全考虑，建议在登录后改变`SessionID`。  
- **`HttpSession`线程不安全**：多线程环境时必须注意属性设定时共享存取的问题。  
- `SessionID`：**默认名称为`JSESSIONID`**，Web容器根据`SessionID`来判断请求所属的会话（浏览器发送`SessionID`给Web容器）找出对应的`HttpSession`。 
    - `HttpSession`的`getId()`方法：获取`HttpSession`的`SessionID`。  
    - 使用`Cookie`：`SessionID`默认使用`Cookie`存放在浏览器中，`Cookie`的名称是`JSESSIONID`。  
    - 使用URI重写：若浏览器禁用`Cookie`，可以使用**URI重写的方法来支持`HttpSession`**。  
> 建议不采用默认的SessionID，在加密联机中传输，设定HTTP-ONLY等，  
> 在用户登录成功之后变更Session以防止客户端被指定了特定的Session ID,
> 从而避免将重要登录凭据等信息存入特定的HttpSession。 
> 会话的重要操作前，最好在进行一次身份确认。  

**HttpSession相关API**：  
```java
public interface HttpSession{
    void invalidate();                                //直接让目前的HttpSession失效。可以在此次会话期间调用这个方法直接让HttpSession失效。                                                        
    void setAttribute(String name, Object value);     
    void getAttribute(String name);
    Enumeration<String> getAttributeNames();
    int getId();                                       //获取SessionID。  默认使用
}
public interface SessionCookieConfig{       //Servlet3.0新增，可设定储存SessionID的Cookie相关信息，必须在ServletContext初始化之前设定。 另一个方法是在web.xml中设定
    void setName(String sessionIdName);     //将默认的Session ID名称修改为别的名称。  
    void setAge(Int second);                //设定储存SessionID的Cookie存活期限。  
}
public interface ServletContext{
    SessionCookieConfig getSessionCookieConfig();   //获取SessionCookieConfig的实现对象。 
    void setSessionTimeout(int second);             //Servlet4.0新增， 设定HttpSession的默认失效时间。 
}
public interface HttpServletRequest extends ServletRequest{
    HttpSession getSession(boolean createIfAbsent);   //返回Session对象，false表示只返回存在的HttpSession对象，没有则返回null。 
    HttpSession getSession();                         //默认为true，true表示若不存在HttpSession对象则创建HttpSession对象。
    String changeSessionId();                         //改变SessionID。
    void setMaxInactiveInterval(Int session);         //设定浏览器多久没有请求应用程序时，HttpSession就失效，单位为秒。
    
    // Servlet3.1以更早的版本需要手动自行取出HttpSession中的属性，然后令目前的HttpSession失效，最后创建新的HttpSession并设定属性。 
    public String changeSessionId(){
        HttpSession oldSession = this.getSession();
        //取出属性
        Map<String, Object> attributes =  new HashMap<>();
        for(String name : Coolections.list(oldSession.getAttributeNames())){
            attributes.putJ(name, oldSession.getAttribute(name));
        }
        //令目前的Session失效
        oldSession.invalidate();
        //创建新的HttpSession并设置属性
        HttpSession newSession = this.getSession();
        for(String name : attributes.keySet()){
            newSession.setAttribute(name, attributes.get(name));
        }
    }   
}

```
**XML配置`HttpSession`**：
```xml
<web-app>
    <session-config>
        <!--设定浏览器多久没有请求应用程序时，HttpSession就失效-->
        <session-timeout>30</session-timeout>
        <!--在ServletContext初始化之前配置SessionCookieConfig-->
        <cookie-config>
            <!-- -->
            <name>youJseSessionId</name>
            <secure>true</secure>
            <http-only>true</http-only>
            <max-age>1800</max-age>
        </cookie-config>
    </session-config>
</web-app>
```

#### `HttpSession`与URI重写  
- `HttpServletResponse`类的`encodeURI(String url)`方法：自动产生带有Session ID的URI重写。
    - 若浏览器没禁用`Cookie`：原样输出URI。  
    - 若浏览器禁用`Cookie`： 添加`SessionID`请求参数到URI。  
- `HttpServletResponse`类的`encodeRedirectURL()`方法：可以为指定的复位向URI编上SessionID。 
- 安全隐患：虽然用户为了隐私权等原因禁用Cookie，但是在URI上直接出现SessionID，反而会有安全上的隐患。  
> 
```java
public interface HttpServletResponse extends ServletResponse{
    String encodeURL(String url);       //自动产生带有Session ID的URI重写。
    String encodeRedirectURL();         //可以为指定的复位向URI编上SessionID。  
}
``` 
