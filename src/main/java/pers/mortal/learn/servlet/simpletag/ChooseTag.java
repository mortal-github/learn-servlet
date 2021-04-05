package pers.mortal.learn.servlet.simpletag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ChooseTag extends SimpleTagSupport {
    private boolean matched;
    public boolean isMatched(){
        return matched;
    }
    public void setMatched(boolean matched){
        this.matched = matched;
    }

    @Override
    public void doTag() throws JspException{
        try{
            this.getJspBody().invoke(null);
        }catch (IOException e){
            throw new JspException("ChooseTag 执行错误", e);
        }
    }
}
