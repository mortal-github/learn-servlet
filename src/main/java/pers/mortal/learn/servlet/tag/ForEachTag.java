package pers.mortal.learn.servlet.tag;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.Collection;
import java.util.Iterator;

public class ForEachTag extends TagSupport {
    private String var;
    private Iterator<Object> iterator;

    public void setVar(String var){
        this.var = var;
    }

    public void setItems(Collection<Object> items){
        this.iterator = items.iterator();
    }

    @Override
    public int doStartTag(){
        if(this.iterator.hasNext()){
            this.pageContext.setAttribute(var, iterator.next());
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

    @Override
    public int doAfterBody(){
        if(iterator.hasNext()){
            this.pageContext.setAttribute(var, iterator.next());
            return EVAL_BODY_AGAIN;
        }
        this.pageContext.removeAttribute(var);
        return SKIP_BODY;
    }
}
