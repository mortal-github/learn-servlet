package pers.mortal.learn.servlet.async;


import org.w3c.dom.ranges.Range;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(
        urlPatterns = {"/async/read_listener"},
        asyncSupported = true
)
public class ReadListenerExample extends HttpServlet {
    private final Pattern fileNameRegex = Pattern.compile("filename=\"(.*)\"");
    private final Pattern fileRangeRegex = Pattern.compile("filename=\".*\"\\r\\n.*\\r\\n\\r\\n(.*+)");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        AsyncContext ctx = request.startAsync();            //启动异步处理

        ServletInputStream in = request.getInputStream();   //获取输入流

        in.setReadListener(new ReadListener(){
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            @Override
            public void onDataAvailable()throws IOException{
               byte[] buffer = new byte[1024];
               int length = -1;
               while(in.isReady() && (length = in.read(buffer)) != -1){
                    out.write(buffer, 0, length);
                }
            }

            @Override
            public void onAllDataRead()throws IOException{
               byte[] content = out.toByteArray();
               String contentAsTxt = new String(content, "UTF-8");

//               String filename = filename(contentAsTxt);
//               Range fileRange = fileRange(contentAsTxt, request.getContentType());
//                write(content, contentAsTxt.substring(0, fileRange.start).getBytes("UTF-8").length,
//                        contentAsTxt.substring(0, fileRange.end).getBytes("UTF-8").length,
//                        String.format("c:/workspace/%s", filename));

                response.getWriter().println("Upload Successfully");
                ctx.complete();             //响应完成
            }

            @Override
            public void onError(Throwable throwable){
                ctx.complete();             //响应完成
                throw new RuntimeException(throwable);
            }
        });
    }
}
