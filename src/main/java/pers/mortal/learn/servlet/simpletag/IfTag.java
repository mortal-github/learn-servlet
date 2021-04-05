package pers.mortal.learn.servlet.simpletag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class IfTag extends SimpleTagSupport {
    private boolean test;

    public void setTest(boolean test){
        this.test = test;
    }

    public void doTag() throws JspException{
        if(test){
            try{
                getJspBody().invoke(null);
            }catch(IOException exception){
                throw new JspException("IfTag 执行错误", exception);
            }
        }
    }
}
