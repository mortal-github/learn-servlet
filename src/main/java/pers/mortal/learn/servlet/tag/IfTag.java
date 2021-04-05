package pers.mortal.learn.servlet.tag;

import javax.servlet.jsp.tagext.TagSupport;

public class IfTag extends TagSupport {
    private boolean test;
    public void setTest(boolean test){
        this.test = test;
    }
    @Override
    public int doStartTag(){
        if(test){
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }
}
