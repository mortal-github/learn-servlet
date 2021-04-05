package pers.mortal.learn.servlet.simpletag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

public class ToUpperCaseTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException{
        StringWriter writer = new StringWriter();
        //用自定义Writer执行标签，以此获得标签内容。
        try{
            this.getJspBody().invoke(writer);
        }catch(IOException e){
            throw new JspException("ToUpperCaseTag 执行错误", e);
        }
        //执行本标签动作ToUpperCase
        String upper = writer.toString().toUpperCase();
        //输出标签内容。
        try{
            this.getJspContext().getOut().print(upper);
        }catch(IOException e){
            throw new JspException("ToUpperCaseTag 执行错误", e);
        }
    }
}
