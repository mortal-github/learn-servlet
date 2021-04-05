package pers.mortal.learn.servlet.simpletag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Collection;

public class ForEachTag extends SimpleTagSupport {
    public String var;
    public Collection<Object> items;

    public void setVar(String var){
        this.var = var;
    }
    public void setItems(Collection<Object> items){
        this.items = items;
    }

    public void doTag(){
        items.forEach(obj->{
            this.getJspContext().setAttribute(var, obj);
            try{
                this.getJspBody().invoke(null);
            }catch(JspException | IOException e){
                throw new RuntimeException(e);
            }
            this.getJspContext().removeAttribute(var);
        });
    }

}
