package pers.mortal.learn.servlet.filter;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;

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
