<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%!
    private String name ;
    private String password;

    public void jspInit(){
        this.name = "钟景文";
        this.password = "密码";
    }

    public void jspDestroy(){
        this.name = null;
        this.password = null;
    }

    public boolean logined(String name, String password){
        if(null == name || null == password)return false;
        return this.name.equals(name) && this.password.equals(password);
    }
%>
<!DOCTYPE html>
<html>
    <head></head>
    <body>
   <% String name = request.getParameter("name");
      String password = request.getParameter("password");
      if(logined(name, password)){
   %>
        <h1>登录成功</h1>
   <%
       }else{
   %>
        <h1>登录失败</h1>
    <%
        }
    %>
    <%= Math.PI%><br>

    <a href="<c:url value="../jstl/core.jsp"/>">URL重写标签</a><br>
    </body>

</html>
