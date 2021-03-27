## Servlet 异步处理  
- 请求线程：Web容器回味每个请求分配一个线程，默认情况下，响应完成前，该线程占用资源不会被释放。  
    - 负载影响：若有些请求需要长时间处理，就会占用Web容器分配的线程，令这些线程无法服务其他请求，从而影响Web应用程序的请求承载能力。 
- 异步处理：Servlet3.0增加异步处理，可以先释放容器分配给请求的线程，令其服务其他请求。  
    - 更换线程：原先释放了容器所分配线程的请求，可以交由应用程序本身分配的线程来处理，然后在对浏览器响应。  
- 搭配其他技术：异步请求常需搭配其他技术来完成，如JavaScript。  

### `AsyncContext`  
- `ServletRequest`的**`startAsync()`**方法：为了支持异步处理，在Servlet3.0的`ServletRequest`上提供了`startAsync()`方法，返回`AsyncContext`对象保存异步处理的上下文。  
    - `AsyncContext startAsync()`:
    - `AsyncContext startAsync(ServletRequest, ServletReponse)`:可以传入自定义请求、响应封装对象。  
- `AsyncContext`类实例：取得`AsyncContext`对象后，此次请求的响应会被延后，Servlet的`service()`方法执行过后就释放容器线程。   
    - `getRequest()`方法：取得请求对象。  
    - `getResponse()`方法：取的响应对象。  
    - **`complete()`**方法：表示响应完成，此次对浏览器的响应将暂缓至调用`complete()`或`dispatch()`方法为止。  
    - **`dispatch()`**方法：表示调派指定的URI进行响应，此次对浏览器的响应将暂缓至调用`complete()`或`dispatch()`方法为止。  
- 配置异步处理：（二选一），过滤器配置相同。 
    - `@WebServlet`注解的**`asyncSuppored`**属性：默认为false，设定为true即告知容器此Servlet支持异步处理。  
    - `web.xml`文件的`<serlvet>`元素的**`<async-supported>`**子元素：设定为true即告知容器此Servlet支持异步处理。  
    

### `AsyncContext`细节  
- 过滤器异步处理：若Servlet进行异步处理，而之前有过滤器，则过滤器亦需要标示其支持异步处理。  
- 异步异常：在不支持异步处理的Servlet或过滤器中调用`startAsync()`方法，会抛出`IllegalStateException`。  
- 过滤流程不变：在支持异步处理的Servlet或过滤器中调用请求对象`startAsync()`方法，完成`service()`方法后，若有过滤器，也会依次序返回，之后释放容器分配的线程。  
- 异步结束：可以调用`AsyncContext`的`complete()`方法完成响应。  
- 转发处理：或者是调用`forward()`方法，将响应转发给其他Servlet/JSP处理，给定路径时相对`ServletContext`的路径。  
- 互斥调用：不可同时调用`complete()`和`forward()`。  
- 单次异步：在两个异步处理的Servlet间**转发前**，不能连续调用两次`startAsync()`，否则抛出`IllegalStateException`。  
- 异步可转同步：将请求从支持异步处理的Servlet转发至一个同步处理的Servlet是可行的，容器会负责调用`AsyncContext`的complete。  
- 同步禁转异步：将请求从一个同步处理的Servlet转发至迟迟异步处理的Servlet是不可行的，会抛出`IllegalStateException`。  
- 异步监听器：如果对`AsyncContext`的起始、完成、超世或错误等事件有兴趣，可以实现**`AsyncListener`**。   
- 添加监听器：`AsyncContext`有个`addListener()`方法，可以加入`AsyncListener`的实现对象，在对应的事件发生时会调用`AsyncListener`实现对象的对应方法。  
- 异步调派：`AsyncContext`的`dispatch()`，将请求调派给别的Servlet，则可以通过请求对象的`getAttribute()`取得以下属性。 
    - `javax.servlet.async.request_uri`:  
    - `javax.servlet.async.context_path`:  
    - `javax.servlet.async.servlet_path`:  
    - `javax.servlet.async.path_info`:  
    - `javax.servlet.async.query_info`:  
    - `javax.servlet.async.mapping`:Servlet4.0新增。  
- `AsyncContext`常数：
    - `AsyncContext.ASYNC_REQUEST_URI`:  
    - `AsyncContext.ASYNC_CONTEXT_PATH`:  
    - `AsyncContext.ASYNC_SERVLET_PATH`:  
    - `AsyncContext.ASYNC_PATH_INFO`:  
    - `AsyncContext.ASYNC_QUERY_INFO`:  
    - `AsyncContext.ASYNC_MAPPINg`:  Servlet4.0新增。  
```java
package javax.servlet;
import java.util.EventListener;

public interface AsyncListener extends EventListener{
    void onStartAsync(AsyncEvent event)throws IOException;
    void onComplete(AsyncEvent event)throws IOException;
    void onTimeout(AsyncEvent event)throws IOExcpetion;
    void onError(AsyncEvent event)throws IoException;
}
```

### 异步`Long Polling`
```java
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
```
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>即时资料</title>
</head>
<body>
    即时资料：<span id="data">0</span>
    <script type="text/javascript">
        function asyncUpdate(){
            let request = new XMLHttpRequest();
            request.onload = function(){
                if(request.status === 200) {
                    document.getElementById('data').innerHTML = request.responseText;
                    asyncUpdate();
                }
            };
            request.open('GET', '../async/long_polling?timestamp=' + new Date().getTime());
            request.send(null);
        }
        asyncUpdate();
    </script>
</body>
</html>
```

### 异步`Server-Sent Event`  
```java
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
```
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>实时资料</title>
</head>
<body>
    实时资料：<span id="data">0</span>
    <script type="text/javascript">
        new EventSource("../async/server-sent_event")
                .addEventListener("message",
                    e => document.getElementById('data').innerHTML = e.data);
    </script>
</body>
</html>
```

### `ReadListener`监听器  
- `ServletInputStream`: 读取上传的资料。然而**输入的读取时阻断式的**，如果因为网络状况不佳，许多时间会耗费在等待数据来到。  
- `ReadListener`实现非阻断式输入：在Servlet3.1中，`ServletInputStream`可以实现非阻断式输入，通过注册一个`ReadListener`实例来达到这种作用。  
- `ReadListener`要求异步Servlet：要注册`ReadListener`实例，必须在异步Servlet中进行。  

**API**：
```java
package javax.servlet;
import java.io.IOException;
import java.util.EventListener;

public interface ReadListener extends EventListener{
    void onDataAvailable() throws IOException;              //在ServletInputStream有数据的时候调用。  
    void onAllDataRead() throws IOException;                //全部数据读取完毕后调用。  
    void onError(Throwable throwable);                      //发生例外时调用
}

public abstract class ServletOutputStream{
    public void setListener(ReadListener listener){}    //添加ReadListener监听器。  
}
```

**示例**：
```java

```


### `WriteListener`监听器  
- `ServletOutputStream`: 输出资料到浏览器。然而**输出的写出是阻断式的**，如果因为网络状况不佳，许多时间会耗费在等待资料输出。  
- `WriteListener`实现非阻断式输出：在Servlet3.1中，`ServletOutputStream`可以实现非阻断式输出，通过注册一个`WriteListener`实例来达到这种作用。  
- `WriteListener`要求异步Servlet：要注册`WriteListener`实例，必须在异步Servlet中进行。  

**API**：
```java
package javax.servlet;
import java.io.IOException;
import java.util.EventListener;

public interface WriteListener{
    void onWritePossible()throws IOException;   //在StreamOutputStream写出时调用
    void onError(Throwable throwable);          //发生例外时调用。  
}
```

**示例**：  
```java

```




