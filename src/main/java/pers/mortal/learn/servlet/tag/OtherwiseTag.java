package pers.mortal.learn.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagSupport;

public class OtherwiseTag extends TagSupport {

    @Override
    public int doStartTag()throws JspException{
        JspTag parent = getParent();
        if(!(parent instanceof  ChooseTag)){
            throw new JspException("必须置于choose标签中");
        }
        ChooseTag choose = (ChooseTag)parent;
        if(choose.isMatched()){
            return SKIP_BODY;
        }
        return EVAL_BODY_INCLUDE;
    }
}
