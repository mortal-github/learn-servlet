package pers.mortal.learn.servlet.simpletag;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class OtherwiseTag extends SimpleTagSupport {
    @Override
    public void doTag()throws JspException{
        JspTag parent = null;
        if(!((parent = getParent()) instanceof ChooseTag)){
            throw new JspTagException("必须置于choose标签中");
        }
        if(((ChooseTag)parent).isMatched()){
            return;
        }
        try{
            this.getJspBody().invoke(null);
        }catch (IOException e){
            throw new JspException("OtherWiseTag 执行错误", e);
        }
    }

}
