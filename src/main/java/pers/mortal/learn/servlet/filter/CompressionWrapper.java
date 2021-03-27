package pers.mortal.learn.servlet.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


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
