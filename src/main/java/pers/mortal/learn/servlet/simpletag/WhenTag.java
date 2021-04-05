package pers.mortal.learn.servlet.simpletag;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class WhenTag extends SimpleTagSupport {
    private boolean test;
    public void setTest(boolean test){
        this.test = test;
    }
    @Override
    public void doTag()throws JspException{
        JspTag parent = null;
        if(! ((parent = getParent()) instanceof ChooseTag)){
            throw new JspException("必须置于choose标签中");
        }
        if(((ChooseTag)parent).isMatched()){
            return;
        }
        if(test){
            ((ChooseTag)parent).setMatched(true);
            try{
                this.getJspBody().invoke(null);
            }catch(IOException e){
                throw new JspException("WhenTag执行错误", e);
            }
        }
    }
}
