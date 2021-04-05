package pers.mortal.learn.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class ToUpperCase extends BodyTagSupport {
    @Override
    public int doEndTag() throws JspException{
        String upper = this.getBodyContent().getString().toUpperCase();
        try{
            pageContext.getOut().write(upper);
        }catch(IOException e){
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }
}
