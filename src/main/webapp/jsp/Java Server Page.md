## JSP  

**JSP与Servlet**：一体两面
> 应该使用JSP编写HTML，而不是在Servlet中编写HTML。  
> JSP最终会转译成Servlet, 只要了解Servlet的功能和特性，就能很好地理解JSP。  
> JSP中直接编写HTML，使用**指示**，**声明**，**脚本(scriptlet)**等许多元素来堆砌各种功能。  

### 从JSP到Servlet  

#### JSP生命周期  
**首次请求JSP**：
> 在第一次请求JSP，容器会进行转译、编译与加载操作。 
  
**JSP的请求方法**：
> 从Java EE的JSP2.3开始，JSPz只接受`GET`、`POST`、`HEAD`请求（定义在`_jspServlet()`中）。  

**`_jspXXX(..)`容器转译方法**：  
- `_jspService(..)`方法：由容器根据JSP内容转译，提供Servlet服务方法。  
- `_jspInit(..)`方法：由容器根据JSP内容转译，提供Servlet初始化方法。  
- `_jspDestroy(..)`方法：由容器根据JSP内容转译，提供Servlet销毁方法。  

**`jspXXX(..)`自定义方法**：  
- `jspInit`(..)：通过继承覆盖该方法来添加初始化操作。  
- `jspDestory`(..)：通过继承覆盖该方法来添加初始化操作。  
> 

**Jsp的隐式对象**：
- 对应变量：对应于转译后的`jspService(..)`方法中的局部变量。  
- 直接使用：在jsp可以直接使用隐式对象。  
- 对应关系：看后面
    - `request`：
    - `response`:
    - `pageContext`:
    - `session`:
    - `application`:
    - `config`:
    - `out`:
    - `page`: 
**Jsp转译成`HttpJspBase`的子类**：
```java
package pers.mortal.learn.servlet.jsp;

import org.apache.jasper.compiler.Localizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.HttpJspPage;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public abstract class ExampleHttpJspBase extends HttpServlet implements HttpJspPage {

    @Override
    public final void init(ServletConfig config) throws ServletException {
        super.init(config);
        jspInit();
        _jspInit();
    }

    @Override
    public String getServletInfo(){
        return Localizer.getMessage("jsp.engine.info");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        _jspService(request, response);
    }

    @Override
    public final void destroy(){
        jspDestroy();
        _jspDestroy();
    }

    //容器自动转译方法
    public void _jspInit(){}
    public void _jspDestroy(){}
    @Override//(HttpJspPage._jspService)
    public void _jspService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String _jspx_method = request.getMethod();
        if(!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)
        && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())){
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
            return;
        }
        //...省略代码
        PageContext pageContext;
        JspWriter out;
        //...省略代码
        try{
            response.setContentType("text/html; charset=UTF-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            //...省略代码
            out = pageContext.getOut();
            _jspx_out = out;
        }catch(Throwable t){
            //...省略代码
        }finally{
            _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
    //自定义方法
    @Override//(HttpJspPage.jspInit)
    public void jspInit(){}
    @Override//(HttpJspPage._jspDestroy)
    public void jspDestroy() {}
}
```

#### 指示元素(Directive)   

- 指示容器：JSP指示元素的主要目的，在于指示容器将JSP转译为Servlet源代码时，一些必须遵守的信息。  
- 指示语法：**`<%@ 指示类型 [属性=”值“]* %>`** 
- 指示类型：有3种指示类型
    - `page`: 告知容器如何转译目前的JSP网页。 
        - `import`属性：在源代码中添加`import`语句。可以在同一个`import`属性中使用逗号分隔多个`import`内容。  
        - `contentType`属性：在转译`JSP`时，必须使用`HttpServletResponse`的`setContentType(String)`方法。  
        - `pageEncoding`属性：JSP文件的编码格式，即输出网页的编码格式。 
        - `info`属性：设置目前JSP的基本信息，转换为`Servlet`程序中使用`getServletInfo()`取得的值。  
        - `autoFlush`属性：默认为`true`，设置输出流是否自动清除。如果设置为`false`,而缓冲区满了却还没调用`flush()`数据送出至浏览器，则引发异常。  
        - `buffer`属性：默认`8kb`，设置至浏览器的输出串流缓冲区大小，必须设置单位。  
        - `errorPage`属性：设置当JSP指定错误而产生异常时，该转发哪一个页面处理这个异常。转发的页面必须为错误处理页面，即`isErrorPage`属性为`true`。 
        - `isErrorPage`属性：设置JSP页面是否为处理异常的页面。与`errorPage`属性配合使用。
        - `extends`属性：指定JSP网页转译为Servlet程序之后，该继承哪一个类，默认为·HttpJspBase`。  
        - `language`属性：指定容器使用哪种语言转译JSP，默认为Java语言。  
        - `isELignored`属性：默认为`false`, 设置JSP网页是否忽略表达式语言EL。这个设置会覆盖`web.xml`的`<el-ignore>`设置。 
        - `isThreadSafe`属性：默认为`true`指示浏览器是否注意线程安全。如果是指为`false`,转译后的`Servlet`会继承`SingleTrheadModel`接口，每次请求时将创建一个`Servlet`实例来处理对象，虽然避免了线程安全问题，但是影响性能。  
    - `include`: 告知容器将指定的JSP网页包括进来转译。这是**静态包括**，意味着在转译期就决定转译后的Servlet内容，多个JSP转译成为一个Servlet。    
        - `file`属性：
    - `taglib`: 告知容器如何转译JSP中的标签。  
  
> 根据`contentType="text/html"`属性和`pageEncoding="UTF-8"`属性设置会在转译JSP时调用`reponse.setContentType("text/html; charset=UTF-8")`。   
```jsp
//header.jspf
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>include 示范开头</title>
    </head>
    <body>
//body.jsp
<%@ include file="/WEB-INF/jspf/header.jspf" %>
    <h1>include 示范本体</h1>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
//foot.jspf 
    </body>
</html>
```

#### `web.xml`配置JSP统一默认设置    
> 可以在web.xml中统一默认的网页编码、内容类型、缓冲区大小等。  
```xml
<jsp-config>
    <jsp-property-group>
        <!--默认page -->
        <url-pattern>*.jsp</url-pattern>
        <page-encoding>UTF-8</page-encoding>
        <default-content-type>text/html</default-content-type>
        <buffer>16kb</buffer>
        <!--默认include，声明指定JSP开头与结尾包括的网页-->
        <include-prelude>/WEB-INF/jspf/pre.jspf</include-prelude>
        <include-coda>/WEB-INF/jspf/coda.jspf</include-coda>
        <!--忽略换行，指示元素之间的换行也会输出到响应中，可以配置忽略这些换行-->
        <trim-directive-whiespace>true</trim-directive-whiespace>
        <!--Scriptlet元素，-->
        <scripting-invalid>true</scripting-invalid><!--禁用JSP上的Scriptlet-->
    </jsp-property-group>
</jsp-config>
```

#### 声明、`Scritlet`与表达式  
> 声明、`Scriptlet`、表达式元素指定转译后的Servlet应该包括哪些**类成员**，**方法声明**，**语句**。  

**声明元素**：  
- 声明语法：`<%! 类成员声明或方法声明 %>`。  
- 声明成员：在`<%! %>`之间的代码，都将转译成`Servlet`中的类成员或方法。  
- 线程问题：使用`<%! %>`声明变量，必须小心数据共享与线程安全的问题（同一Servlet服务多个请求）。  
- 重载操作：使用`<%! %>`声明`jspInit()`与`jspDestory()`就会在转译后的Servlet中出现相对应的方法片段。  

```jsp 
<%!
    String name = "caterpillar";
    String password = "123456";
    boolean checkUser(String name, String password){
        return this.name.equals(name) && this.password.equals(password);
    }
%>
```
```java
package org.apache.jsp;

public final class index_jsp extends HttpJspBase{
    String name = "caterpillar";
    String password = "123456";
    boolean checkUser(String name, String password){
        return this.name.equals(name) && this.password.equals(password);
    }
}
```

**`Scriptlet`元素**：  
- 代码语法：`<% Java语句 %>`。  
- 转译代码：在`<% %>`之间的内容，将被转译为`Servlet`源代码`_jspService()`方法中的内容。  
- `HTML`内容：直接在JSP中编写的`HTML`,都会变成等`out`对象输出的内容。 
- 转译顺序：`Scriptlet`出现的顺序也就是在转译为`Servlet`后，语句出现在`_jspService()`中次序。 

```jsp
<%
    String name = request.getParameter("name");
    String password = reqeust.getParameter("password");
    if(checkUser(name, password)){
%>
    <h1>登录成功</h1>
<%
    }else{
%>
    <h1>登录失败</h1>
<%
    }
%>
```
```java
package org.apacke.jsp;

public final class login_jsp extends HttpJspBase{
    public void _jspService(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        //...省略代码
        String name = request.getParameter("name");
        String password = reqeust.getParameter("password");
        if(checkUser(name, password)){
            out.writer("\n");
            out.writer("    <h1>登录成功</h1>");
        }else{
            out.writer("    <h1>登录失败</h1>");
        }
        //...省略代码
    }
}
```

**表达式元素**：
- 表达式语法：`<%= Java表达式 %>`。  
- 表达式结果：表达式的运算结果将直接输出为网页的一部风。  
- 表达式转译：表达式不用加上分号(;)。会在`_jspService(..)`中转译为`out.println(Java表达式)`。  

```jsp
<%= LocalDateTime.now() %>
```
```java
package org.apacke.jsp;

public final class login_jsp extends HttpJspBase{
    public void _jspService(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        //...省略代码
        out.print(LocalDateTime.now());
//...省略代码
    }
}
```

#### 注释元素

- Java注释：`<% %>`直接使用Java语法编写程序，故在其中可用Java注释。但是该注释会输出到Servlet源代码中。   
- HTML注释：HTML代码中可使用`<!--注释-->`来注释。但是该注释会输出到HTML网页中，即转译为`out.write(<!-- 网页注释 -->)`。  
- JSP注释： `<%-- JSP注释 -->`，JSP的专用注释，转译JSP至Servlet时会忽略`<%-- -->`之间的文字。生成的Servlet不包括注释文字，也不会输出至浏览器。  

#### 隐式对象  

**`JSP`隐式对象与`_jspService()`局部变量对应关系**：
|隐式对象|局部变量|  
|:---|:---|  
|`request`|转译后对应`HttpServletRequest`对象|  
|`response`|转译后对应`HttpServletResoponse`对象|  
|`pageContext`|转译后对应`PageContext`对象，提供了JSP页面资源的封装，并可设置页面范围属性|  
|`application`|转译后对应`ServletContext`对象|  
|`config`|转译后对应`ServletConfig`对象|  
|`session`|转译后对应`HttpSession`|  
|`out`|转译后对应`JspWripter`对象，内部关联一个`PrintWriter`对象|  
|`page`|转译后对应`this`|  
|`exception`|转译后对应`Throwable`对象，代表由其他JSP页面抛出的异常，只会出现在错误页面(`isErrorPage="true"`)|

**`JspWriter`**：有缓冲功能。  
- 若JSP页面没有缓冲，直接使用`PrintWriter`输出。  
- 若JSP页面有缓冲，只有在清除(flush)缓冲区时，才真正创建`PrintWriter`输出。  

**缓冲区满的处理决策**：
- 累计缓冲区的容量后再一次输出响应，所以缓冲区满了就直接清除。  
- 控制输出量在缓冲区容量之内，所以缓冲区满了表示错误，抛出异常。   

**`pageContext`对象**：
- 获取隐式对象：`pageContext`封装了所有的JSP页面信息，所有隐式对象都可用`pageContext`来获取。  
- 设置页面属性：`pageContext`设置页面属性，默认是可设置或取得页面范围属性，表示作用范围仅限于同一页面。  
- 设置四种属性：实际上可以通过`pageContext`设置四种范围属性。

```java
public class pageContext{
    public static final int PAGE_SCOPE;
    public static final int REQUEST_SCOPE;
    public static final int SESSION_SCOPE;
    public static final int APPLICATION_SCOPE;
    //在page范围内设置、获取、清除属性。
    public void setAttribute(String name, Object value);
    public Object getAttribute(String name);
    public void removeAttribute(String name);
    //在指定范围内设置、获取、清除属性。
    public void setAttribute(String name, Object value, int scope);
    public Object getAttribute(String name, int scope);
    public void removeAttribute(String name, int scope);
    //依次从页面、请求、会话、应用程序范围查找属性，找到则返回。 
    public Object findAttribute(String name);
    
}
```

#### 错误处理  

**错误发生点**：  
- 转译时：JSP页面中编写了一些错误语法。  
- 编译时：Servlet源代码编译是。`JSP`语法没问题，成功转译出`.java`文件，但是编译`.java`出问题（如JSP使用了某些了，部署至服务器时忘记部署相关类）。  
- 运行时：Servlet编译成功，载入容器运行时，因资源或逻辑问题而发生错误。  

**错误处理**：
- `page`指示器的`isErrorPage`：定义该页面为错误处理页面（不可出现`errorPage`属性）。  
- `page`指示器的`errorPage`：定义该页面出现错误时，应该转向的错误处理页面（不可出现`isErrorPage`属性）。  
- `exception`隐式对象：只能出现在错误处理页面（`isErrorPage=true`的页面），且表示发生错误的页面（出现`errorPage`的页面）抛出的异常。  
- 自动处理：如果没有处理异常，容器则会自动处理，直接显示异常信息与堆栈跟踪信息。  
- `web.xml`的`<error-page>`：可在`web.xml`中指定，发生错误时，容器自动转发的URI。  

```xml
<web-app>
  <error-page>
    <exception-type>java.lang.NullPointerException</exception-type>
    <location>/report.view</location><!--该页面必须设置isErrorPage属性为true,才可使用exception隐式对象-->
  </error-page>
  <error-page>
    <error-code>404</error-code><!--基于HTTP错误状态码转发至处理页面-->
    <location>/404.jsp</location>
  </error-page>
</web-app>
```

### 标准标签
- 减少`Scriptlet`: JSP规范提供标准标签,容器都支持,可协助编写JSP时,减少`Scriptlet`的使用。  
- `jsp:`前缀:标准标签都以`jsp:`为前缀。  

#### `<jsp:include>`、`<jsp:forward>`标签   
> 在转译的Servlet源代码中，取得`RequestDispatcher`对象调用对应的`include`、`forward`方法。  
> `<jsp:include>`包括的多个页面独立转译成Servlet，而`include`指示元素包括的多个页面转译成一个Servlet。  
> 可以在`<% %>`中使用`pageContext`取得`RequestDispatcher`对象手动`include`或`forward`。  

```jsp
<jsp:include page="add.jsp">
    <jsp:param name="a" value="1"/><%--传入调派的Servlet的请求参数--%>
    <jsp:param name="b" value="2"/>
</jsp:include>
<jsp:forward page="add.jsp">
    <jsp:param name="a" value="1"/>
    <jsp:param name="b" value="1"/>
</jsp:forward>
``` 

#### `<jsp:useBean>`、`<jsp:setProperty>`、`<jsp:getProperty>`标签  
> `<jsp:useBean>`搭配满足以下条件的JavaBean元件。  
> - 必须实现`java.io.Serializable`接口。  
> - 没有公开的类变量。  
> - 具有无参构造器。  
> - 具有公开的Setter方法和Getter方法。  
> 使用`<jsp:useBean>`搭配JavaBean，然后使用`<jsp:setProperty>`和`<jsp:getProperty>`对JavaBean进行设置和取值，可减少`Scriptlet`的使用。  

**示例**：
```jsp
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="user" class="package.User"/>
<jsp:setProperty name="user" property="*"/><%--使用与Setter方法名对应的请求参数来设置值，setXyz对应请求参数xyz--%>
<!DOCTYPE html>
<html>
    <head></head>
    <body>
        <%
            if(user.isValid()){ <%--user名称是根据&gt;jsp:userBean&lt;上的id名称而来--%>
        %>
        <h1>
            <jsp:getProperty name="user" property="name"/><%--使用对应的Getter方法名的Getter方法获取值，getXyz对应property属性的xyz值--%>
        </h1>
    </body>
</html>
```

**`<jsp:useBean>`**：  
- `id`：指定JavaBean实例的名称，在JSP中可根据id名取得这个对象。  
- `class`：实例化JavaBean的类型。  
- `scope`：指定JavaBean所属的属性范围。先查找看看指定属性范围是否有JavaBean的属性存在，若无再创建JavaBean。  
- `type`：声明引用JavaBean实例的变量的类型，如抽象类，接口。  
> 实际上就是在声明一个JavaBean对象，并设置为页面范围属性。  
> 也可以在标签body中使用`<jsp:setProperty>`一并设置属性。  
```jsp
<jsp:useBean id="user" class="package.User" scope="page" type="package.UserSuperType">
    <jsp:setProperty name="user" property="*"/>
</jsp:useBean>
``` 

**`<jsp:setProperty>`**：  
- `name`属性： 使用JavaBean实例的id名称取得JavaBean实例。  
- `property`属性：调用对应的`setXXX()`方法来设置属性值。若为`*`，则表示自动寻找对应Setter方法名的请求参数来设置属性。  
- `value`属性：与`property`属性配合，设置属性值。  
- `param`属性：与`property`属性配合，用请求参数来设置属性值。  
> 注意`property`属性不一定是真实的域名，指示对应Setter方法名中`set`字符串之后的子串。  
> 使用`pageContext`的`findAttribute(name)`查找属性，然后使用反射查找setXXX()方法。 
> 也可以不指定`value`和`param`，此时将会使用`property`值来查找请求参数，然后设置属性。  

**`<jsp:getProperty>`**：  
- `name`属性： 使用JavaBean实例的id名称取得JavaBean实例。  
- `property`属性：调用对应的`getXXX()`方法来获取属性值。  

**JSP语法的XML格式**：  

|JSP语法|XML格式语法|
|:---|:---|
|`<%@ page import="package.type" %>`|`<jsp:directive.page import="package.type">`|
|`<%! String name %>`|`<jsp:declaration> String name; </jsp:declaration>`|
|`<% name = "zhongjignwen" %>`|`<jsp:scriptlet> name = "zhongjingwen" </jsp:scriptlet>`|
|`<%= name %>`|`<jsp:expression> name </jsp:expression>`|
|网页文字|`<jsp:text>网页问题`</jsp:text>`|

### EL表达式语言  
**使用EL减少`Scriptlet`**：  
- 取得简单的属性、请求参数、标头、`Cookie`。  
- 简单的运算或判断。  
- EL函数：也可以将一些常用的公用函数编写为EL函数。  
- 调用静态成员：设置使用EL3.0直接调用静态变量。  

**EL表达式**：
- `${ EL表达式 }`：EL表达式位于`${ }`之间。  
- `.`运算：取得属性（通过调用对应的setXXX方法）或调用方法，可连续使用。  
- `+`运算：加法运算。  
- `null`处理：对于`null`值直接以空字符串加以显示，而不是显示`null`值，且进行运算时，不会因此发生错误而抛出异常。  
- 自动转换类型：如果必须转换类型，EL会自动转换。  

**禁用EL表达式**：  
- `page`指示元素的`ieELIgnored`属性：默认为`false`。  
- `web.xml`的`<el-ignored>`元素：  
```xml
<jsp-config>
    <jsp-property-group>
        <url-pattern>*.jsp</url-pattern>
        <el-ignored>true</el-ignored>
    </jsp-property-group>
</jsp-config>
```

**使用EL取得属性**： 
- `.`运算符：左边可以是JavaBean（对应getXXX方法）或Map对象（对应键名）。  
- `[]`运算符：左边可以是JavaBean、Map、数组或List对象。  
    - 运算：`[]`运算符中不是双引号时，会尝试做运算，结果再给`[]`来使用。  
    - 嵌套：`[]`运算符可以嵌套。 
> 若不指定属性的存在范围，默认是以`page`、`request`、`session`、`application`顺序查找属性。  
> 某些情况下`.`和`[]`可以互换。 
> 当左边是Map时，建议使用`[]`运算符，因为若键名称有空白或点字符，这是可以正确取值的方法。  
```jsp
${user.name}<%--调用user对象的getName()方法--%>
${array[0]}<%--访问元素--%>

<%--某些请况下，可以互换-->
${user.name}
${user["name"]}

<%--运算--%>
${name[param.index]}
${name[0]}

<%--嵌套--%>
${datas[names[param.index]]}
```

**EL隐式对象**：
- `pageContext`: 表示`PageContext`对象。  
- 范围属性对象：这些EL隐式对象与JSP隐式对象不同，它仅代表作用范围。  
    - `pageScope`: 默认从`pageScope`的属性开始寻找。   
    - `requestScope`
    - `sessionScope`
    - `applicatonScope`
- 请求参数相关对象：  
    - `param`: 表示请求参数。   
    - `paramValues`: 相当于`request.getParameterValues(name)`，使用`[]`运算指定取哪个值。  
- `cookie`: 表示`cookie`对象。     
- `initParam`: 取得`web.xml`设置的`ServletContext`初始参数。  
```jsp
${param.favorite}
${paramValus.favorites[0]}<%--相当于request.getPrameterValues("favorites").next()--%>

${cookie.username}<%--相当于cookie.getValue("username")--%>

${initParam.initCount}<%--相当于servletContext.getInitParameter("initCount")--%>
```
    
**EL运算符**： 
- 算术运算符：  
    - `+`  
    - `-`  
    - `*`  
    - `/`或`div`  
    - `%`或`mod`  
- `? :`运算符  
- 逻辑运算：  
    - `and`  
    - `or`  
    - `not`  
- 关系运算符：  
    - `<`或`lt`  
    - `>`或`gt`  
    - `<=`或`le`  
    - `>=`或`ge`  
    - `==`或`eq`  
    - `!=`或`ne` 
- `()`运算符     
> 关系运算符也可比较字符或字符串，按字符编码表的编码数字依次比较。  
> `==`、`eq`、`！=`、`ne`也可判断`null`。  
> 如果操作数是一个代表数字的字符串，会尝试剖析为数值再进行运算，如：`${'4' > 3}`  
> EL运算符优先级与Java运算符优先级对应，也可以使用`()`括号决定自定义先后顺序。  

**自定义EL函数**： 
1. 编写公开类，实现公开且静态的方法。  
2. 编写标签程序库描述TLD文件，XML文件，后缀名为`tld`。  
3. 在JSP页面中使用`taglib`指示元素，指示容器寻找读取TLD中的标签描述（可通过`uri`属性对应`<uri>`标签）。  


```java
public class util{
    public static int length(Collection collection){
    return collection.size();
    }
}
```
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<taglib version="2.1" xmlns="http://java.sun.com/xml/ns/javaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee 
        http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd">
    <tlib-version>1.0</tlib-version>
    <short-name>openhame</short-name>
    <uri>https://openhome.cc/util</uri>
    <function>
        <description>Collection Length</description>
        <name>length</name>
        <function-class>cc.openhome.Util</function-class>
        <function-signature>int length(java.util.Collection)</function-signature>
    </function>
</taglib>
```
```jsp
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ tablig prefix="util" uri="https://openhome.cc/util" %>
<!DOCTYPE html>
<html>
    <head></head>
    <body>
        ${util:length([100, 95, 88, 75])} <%--使用自定义EL函数--%>
    </body>
</html>
```

**EL3.0**:  
- 允许指定变量：`${a = "10"}`，实际上变量会被指定为页面范围属性。  
- 允许建立`List`: `${list = [value1, value2, ..., valueN]}`。  
- 允许建立`Set`:  `${set = {value1, value2, ..., valueN}}`。  
- 允许建立`Map`: `${map = {key1:value1, key2:value2, ..., keyN:valueN}}`。   
- `+=`串接字符床：只是用来区分`+`表示连接字符串，如：`${firstName += lastNname}`。  
- 呼叫对象：`${name = "Justin"} ${name.toUpperCase()}`。  
- 不显示`void`: 如果调用方法没有传回值，那么就不会显示结果，如：`${pageContext.setAttribute("token", "123"}`。  
- 调用静态成员：可以直接调用静态成员或静态方法，默认`java.lang`中的类可以直接调用，如：`${Integer.parseInt("123")} ${Math.PI}`。  
- 导入类以便调用静态成员：`${pageContext.ELContext.importHandler.importClass("java.time.LocalTime")} ${LocalTime.now()}`。  
- `Lambda`表达式：`${plus = (x,y) -> x+y} ${plus(10, 20)} ${()-> plus(10,20) + plus(30,40)}`。  

```jsp
${a = "10"}
${list = [value1, value2, ..., valueN]}
${set = {value1, value2, ..., valueN}}
${map = {key1:value1, key2:value2, ..., keyN:valueN}}

<%--串接字符串--%>
${firstName += lastNname}
${a = "10"}
${b = "20"}
${a + b} <%--进行加法运算，结果为30。 此时会剖析a,b字符串代表的数值，若无法剖析则引发NumberFormatException--%>
${a += b} <%--连接字符串，结果为"1020"--%>
<%--呼叫对象--%>
${name = "Justin"} 
${name.toUpperCase()}

<%--调用结果没有返回值则不显示--%>
${pageContext.setAttribute("token", "123"}

<%--调用静态域或静态方法--%>
${Integer.parseInt("123")} 
${Math.PI}

<%--导入类--%>
${pageContext.ELContext.importHandler.importClass("java.time.LocalTime")} 
${LocalTime.now()}

<%--Lambda表达式--%>
${plus = (x,y) -> x+y} 
${plus(10, 20)} 
${()-> plus(10,20) + plus(30,40)}

<%--Java SE8 的流畅stream风格--%>
${names = ["Justin", "Monica", "Irene"]}
${names.stream().filter(name - >name.leangth() == 5).toList()}
```