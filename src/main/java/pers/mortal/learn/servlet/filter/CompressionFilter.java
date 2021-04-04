package pers.mortal.learn.servlet.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
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
