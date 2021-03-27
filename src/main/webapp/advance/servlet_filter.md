## Servlet 过滤器  

**`Servlet/JSP`的过滤器机制**：
- **过滤器**：介于Servlet之前，可拦截浏览器对Servlet的请求；也可以改变Servlet对浏览器的响应。  
- **抽换功能**：将额外的功能集中的过滤器中，可以视需求抽换这些功能。  
- **过滤顺序**：可以调整过滤器的顺序。  
- **指定过滤**：可以针对不同的URI或Servlet应用不同的过滤器。 
- **调派间过滤**：甚至可以在Servlet间请求转发和包含时应用过滤器。  

**过滤器接口**：
- `Filter`接口：Servlet4.0之前必须实现Filter接口。   
- `GenericFitler`类：Servlet4.0之后，增加了`GenericFilter`类，继承实现了`Filter`、`FilterConfig`。（与GenericServlet类似，封装Filter配置）  
    - 无参`init()`方法：若是`GenericFilter`的子类，要**定义`Filter`的初始化**，可以重新定义无参数的`init()`方法。  
- `HttpFilter`类：Servlet4.0，增加了`HttpFilter`，继承自`GenericFilter`，对于HTTP方法的处理**增加了另一个版本的`doFilter()`方法**。  

> FilterChain实例的doFilter()方法运行下一个过滤器，若没有下一个过滤器，就调用请求目标Servlet的service()方法。  
> 如果因为某个原因而没有调用FilterChain的doFilter()，则请求就不会继续交给接下来的过滤器或目标Servlet，也即是拦截请求。  
> FilterChain工作流程：
> service()前置处理：在FilterChain参数实例的doFilter()方法前的代码运行在service()方法前。  
> 堆栈顺序返回：在陆续调用用Filter实例的doFilter()乃至Servlet的service()方法之后，流程就会以堆栈顺序返回，  
> service()后置处理：所以在FilterChain的doFilter()运行完毕之后就可以针对service()方法做后续处理。    
> 独立设计过滤器：
> 只需知道FilterChain运行后回忆堆栈顺序返回即可。  
> 在实现Filter接口时，不用理会这个Filter前后是否有其他Filter,应该将之作为一个独立的元件来设计。    
> 过滤异常：
> 如果在调用Filter的doFilter()期间，因故抛出UnavailableException,此时不会继续下一个Filter， 
> 容器可以检验异常的isPermanent()，如果不是true,可以在稍后重试Filter。  

**过滤器接口API**：
```java
package javax.servlet;
import java.io.IOException;

public interface Filter{
    default void init(FilterConfig config) throws ServletException{}            //过滤器初始化i 
    default void doFilter(ServletRequest request, ServletResponse response,     //过滤方法，FilterChain链接了下一个Filter或Servlet。 
            FilterChain chain)                                                  
            throws IOException, ServletException{}                              
    default void destroy(){}
}
public interface FilterConfig{
    String getFilterName();
    ServletContext getServletContext();
    String getInitParameter(String name);
    Enumeration<String> getInitParameterNames();
}
public class FilterChain{
    public void doFilter(ServletRequest request, ServletResponse response){
        Filter filter = filterIterator.next();
        if(filter != null){
            filter.doFilter(request, response, this);
        }else{
            targetServlet.service(request, response);
        }
    }
}

public abstract class GenericFilter implements Filter, FilterConfig, Serializable{
    private static final long serialVersionID = 1L;
    private volatile FilterConfig filterConfig;
    
    public FilterConfig getFilterConfig(){
            return filterConfig;
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException{
        this.filterConfig = filterConfig;
        init();
    }
    
    public void init()throws ServletException{}         //通过重定义此方法可自定义初始化操作。
    
    @Override
    public String getFilterName(){
        return getFilterConfig().getFilterName();
    }

    @Override
    public ServletContext getServletContext(){
        return getFilterConfig().getServletContext();
    }  

    @Override
    public String getInitParameter(String name){
        return getFilterConfig().getInitParameter(name);   
    }

    @Override
    public Enumeration<String> getInitParameterNames(){
        return getFilterConfig().getInitParameterNames();
    }
   
}
```
```java
package javax.servlet.http;

public abstract class HttpFilter extends GenericFilter{
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException{
        if(!(request instanceof HttpServletRequest)){
            throw new ServletException(request + " not HttpServletRequest");
        }
        if(!(response instanceof HttpServletResponse)){
            throw new ServletException(response + " not HttpServletResponse");
        }
    
        doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }

    public void doFilter( HttpServletRequest request,  HttpServletResponse response, FilterChain chain)
                    throws ServletException, IOException{
        chain.doFilter(request, response);
    }
}
```

**过滤器配置**
- `@WebFilter`注解配置。
- `web.xml`文件的XML配置。
- 对比规则：
    > 如果同时具备`<url-pattern>`和`<servlet-name>`,注解相同配置也遵循如下规则，   
    > 先对比`<url-pattern>`再对比`<servlet-name>`。  
- 过滤顺序：
    > 如果由某个URI或Servlet会应用多个过滤器，  
    > 根据`<filter-mapping>`在web.xml中出现的先后顺序，来决定过滤器的运行顺序。 
     
```java
@WebFilter(
        filterName = "filterExample",
        urlPatterns = "/*",                         //设置哪些URI请求必须应用哪个过滤器，"/*"表示应用在所有URI请求上。
        servletNames = {"ServletInitParameter"},    //除了自定URI模式，也可以指定Servlet名称。
        initParams = {                              //可以设置过滤器初始化参数。
                @WebInitParam(name = "PARAM1", value = "VALUE1"),
                @WebInitParam(name = "PARAM2", value = "VALUE2")
        },
       dispatcherTypes = {                          //触发过滤器的时机，默认是由浏览器直接出发请求。
               DispatcherType.REQUEST,              //默认
               DispatcherType.INCLUDE,              //通过RequestDispatcher的include()请求而来  
               DispatcherType.FORWARD,              //通过RequestDispatcher的forward()请求而来
               DispatcherType.ERROR,                //指容器处理意外而转发过来的请求。
               DispatcherType.ASYNC                 //指异步处理的请求。
       }
)
public class FilterExample extends HttpFilter {
    private String PARAM1;
    private String PARAM2;
    
    @Override
    public void init()throws ServletException{      //初始化操作
        PARAM1 = getInitParameter("PARAM1");
        PARAM2 = getInitParameter("PARAM2");
    }
}
```
       
```xml
<web-app>
 <!--过滤器-->
  <filter>
      <filter-name>filterExample</filter-name>
      <filter-class>pers.mortal.learn.servlet.filter.FilterExample</filter-class>
      <init-param>
        <param-name>PARAM1</param-name>
        <param-value>VALUE1</param-value>
      </init-param>
    <init-param>
      <param-name>PARAM2</param-name>
      <param-value>VALUE2</param-value>
    </init-param>
  </filter>
  <filter-mapping>
      <filter-name>filterExample</filter-name>
      <url-pattern>/*</url-pattern>
      <servlet-name>ServletInitParameter</servlet-name>
      <dispatcher>REQUEST</dispatcher>
      <dispatcher>FORWARD</dispatcher>
      <dispatcher>INCLUDE</dispatcher>
      <dispatcher>ERROR</dispatcher>
      <dispatcher>ASYNC</dispatcher>
  </filter-mapping>
</web-app>
```

### 请求封装器和响应封装器  

- 过滤器功能限制：
    - `HttpServletRequest`修改限制：对于容器产生的`HttpServletRequest`对象,无法直接修改某些信息，如请求参数值（有时候为了安全需要对其转义）。  
    - `HttpServletResponse`修改限制：例如压缩输出，无法在过滤器中直接控制。  
- 封装器：
    - 实现请求响应接口：封装器实现`HttpServletRequest`或`HttpServletResponse`接口。  
    - 改变请求响应方法：可以定义一个封装器，封装请求响应对象，通过重定义请求响应中某些方法从而达到修改信息的目的。  
    - 代替请求响应对象：通过在过滤器中的`FilterChain`的`doFilter`传入封装器，就可以让封装器代替请求响应对象。   
- 封装器接口：
    - `ServletRequestWrapper`类：实现`ServletRequest`接口。  
    - `HttpServletRequestWrapper`类：继承`ServletRequestWrapper`类，实现`HttpServletRequest`接口。   
    - `ServletResponseWrapper`类：实现`ServletResponse`接口。   
    - `HttpServletResponseWrapper`类：继承`ServletResposnseWrapper`类，实现`HttpServletResponse`接口。   
- 定义封装器：
    - 继承：继承实现`HttpServletRequestWrappser`或`HttpServletResponse`。  
    - `super()`构造器：构造器接受原始的请求/响应对象，**必须使用`super()`**调用超类构造接口原始的请求/响应对象，
        - 取得原始对象：`getRequest`或`getResponse`方法可以取得原始的请求/响应对象。 
    - 覆盖方法以修改内容：覆盖指定方法以便修改内容。
        > 例如覆盖getParameter()方法，就可以对请求参数值转义。  
        > 覆盖getOutputStream()方法，就可以返回一个组合了原始输出流的压缩输出流，从而改变输出。  
         
**请求封装器示例**：                                 
```java
@WebFilter("/*")
public class Encoder extends HttpFilter{
    public void doFilter(HttpServletRequest request, HttpServletResponse response){
        EncoderWrapper requestEncoderWrapper = new EncoderWrapper(request);//将原请求对象包裹在EncoderWrapper中。
        chain.doFilter(requestEncoderWrapper, response);
    }
}
public class EncodeWrapper extends HttpServletRequestWrapper {
    public EncodeWrapper(HttpServletRequest request){
        super(request);
    }

    @Override
    public String getParameter(String name){
        ServletRequest request = this.getRequest();
        return Optional.ofNullable(request.getParameter(name)).map(Encode::forHtml).orElse(null);
    }

}
```

**响应封装器示例**：
```java
//通过过滤器使用输出压缩响应封装器
@WebServlet("/*")
public class CompressionFilter extends HttpFilter {
    
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException{
        
        String encodings = request.getHeader("Accept-Encoding");
        if(encodings != null && encodings.contains("gzip")){//检查是否接受gzip压缩格式
            CompressionWrapper responseCompressionWrapper = new CompressionWrapper(response);
            responseCompressionWrapper.setHeader("Content-Encoding", "gzip");//设置响应格式为gzip。
            
            chain.doFilter(request, responseCompressionWrapper);//运行下一个过滤器
            
            responseCompressionWrapper.finish();//完成压缩输出。
        }else{//不接受压缩，直接进行下一个过滤器。
            chain.doFilter(request, response);
        }
    }
}
//压缩输出响应封装器
public class CompressionWrapper extends HttpServletResponseWrapper {
    private GzipServletOutputStream gzipServletOutputStream;
    private PrintWriter printWriter;

    public CompressionWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException{
        if(this.printWriter != null){
            throw new IllegalStateException();
        }
        if(this.gzipServletOutputStream != null){
            ServletResponse response = this.getResponse();
            this.gzipServletOutputStream = new GzipServletOutputStream(response.getOutputStream());
        }
        return this.gzipServletOutputStream;
    }

    @Override
    public PrintWriter getWriter()throws IOException{
        if(this.gzipServletOutputStream != null){
            throw new IllegalStateException();
        }
        if(this.printWriter != null){
            ServletResponse response = this.getResponse();
            this.gzipServletOutputStream = new GzipServletOutputStream(response.getOutputStream());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(gzipServletOutputStream, response.getCharacterEncoding());
            printWriter = new PrintWriter(outputStreamWriter);
        }
        return this.printWriter;
    }

    @Override
    public void flushBuffer() throws IOException{
        if(this.printWriter != null){
            this.printWriter.flush();
        }else if(this.gzipServletOutputStream != null){
            this.gzipServletOutputStream.finish();
        }
        super.flushBuffer();
    }

    public void finish()throws IOException{
        if(this.printWriter != null) {
            this.printWriter.close();
        }else if(this.gzipServletOutputStream != null){
            this.gzipServletOutputStream.finish();
        }
    }

    //不实现方法内容，因为真正的输出会被压缩。
    @Override
    public void setContentLength(int length){}
    @Override
    public void setContentLengthLong(long length){}
}
//压缩输出流
public class GzipServletOutputStream extends ServletOutputStream {
    private ServletOutputStream servletOutputStream;
    private GZIPOutputStream gzipOutputStream;

    public GzipServletOutputStream(ServletOutputStream servletOutputStream)throws IOException{
        this.servletOutputStream = servletOutputStream;
        this.gzipOutputStream = new GZIPOutputStream(servletOutputStream);//使用GZIPOutputStream增加压缩功能
    }

    public GZIPOutputStream getGzipOutputStream(){
        return this.gzipOutputStream;
    }

    @Override
    public void write(int b) throws IOException {//输出时通过GZIPOutputStream的writer()压缩输出。
        this.gzipOutputStream.write(b);
    }

    @Override
    public boolean isReady() {
        return this.servletOutputStream.isReady();
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        this.servletOutputStream.setWriteListener(writeListener);
    }

    @Override
    public void close() throws IOException{
        this.gzipOutputStream.close();
    }

    @Override
    public void flush() throws IOException{
        this.gzipOutputStream.flush();
    }
    
    public void finish() throws IOException{
        this.gzipOutputStream.finish();
    }
}
```
                                                                  
    