package pers.mortal.learn.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class ChooseTag extends TagSupport {
    private boolean matched;
    public boolean isMatched(){
        return this.matched;
    }
    public void setMatched(boolean matched){
        this.matched = matched;
    }

    @Override
    public int doStartTag()throws JspException{
        matched = false;
        return EVAL_BODY_INCLUDE;
    }
}
