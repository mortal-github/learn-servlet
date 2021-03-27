package pers.mortal.learn.servlet.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

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
