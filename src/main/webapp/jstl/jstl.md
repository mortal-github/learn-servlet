## JSTL  
**添加JSTL依赖**：
```xml
<dependencies>
    <!--JSTL标签接口与类-->
    <dependency>
      <groupId>org.apache.taglibs</groupId>
      <artifactId>taglibs-standard-impl</artifactId>
      <version>1.2.5</version>
    </dependency>
    <!--JSTL实现-->
    <dependency>
      <groupId>org.apache.taglibs</groupId>
      <artifactId>taglibs-standard-spec</artifactId>
      <version>1.2.5</version>
    </dependency>
</dependencies>
```
**JSTL概述**：
- JSTL是另一个标准规范，并非在JSP的规范中。  
- JSTL标签库分为五大类：  
    - 核心标签库，提供条件判断，属性访问，URI处理及错误处理等标签。  
    - I18N兼容格式标签库：提供数字、日期等的格式化功能，以及区域、信息、编码处理等国际化功能的标签。  
    - SQL标签库：提供基本的数据库查询、更新、设置数据源(DataSource)等功能的标签。  
    - XML标签库：提供XML解析、流程控制、转换等功能的标签。  
    - 函数标签库：提供常用字串处理的自定义EL函数等功能标签库。  
    
### 核心标签库  
```jsp
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>登录页面</title>
    </head>
    <body>
        <%-- 流程控制标签 --%>
        <c:if test="${param.name == '钟景文' && param.password == '密码'}">
            <h1>${param.name}登录成功</h1>
        </c:if>
        <c:choose>
            <c:when test="${param.valid == 'true'}">
                <h1>${param.name}</h1>
            </c:when>
            <c:otherwise>
                <h1>登录失败</h1>
            </c:otherwise>
        </c:choose>

        <c:forEach var="token" items="Java,C++,C,JavaScript">
                ${token} <br>
        </c:forEach>
        <c:forTokens var="token" delims=":" items="Java:C++:C:JavaScript">
              ${token} <br>
        </c:forTokens>


        <%-- 错误处理标签 --%>
        <c:catch var="error">
            ${param.a} + ${param.b} = ${param.a + param.b}
        </c:catch>
        <c:if test="${error != null}">
            <br><span style="color: red;">${error.message}</span>
            <br>${error}
        </c:if>


        <%-- 网页导入、重定向、URI重写标签--%>
        <c:import url="../jsp/jsp.jsp" charEncoding="UTF-8">
            <c:param name="name" value="${param.name}"/>
            <c:param name="password" value="${param.password}"/>
        </c:import>
        <c:if test="${param.name != '钟景文'}">
            <c:redirect url="../jsp/jsp.jsp" >
                <c:param name="name" value="${param.name}"/>
                <c:param name="password" value="${param.password}"/>
            </c:redirect>
        </c:if>

        <a href=<c:url value="../jsp/jsp.jsp"/>>URL重写标签</a><br>
        <a href="<c:url value="../jsp/jsp.jsp"/>">URL重写标签</a><br>
        <c:url value="../jsp/jsp.jsp">
            <c:param name="name" value="${name}"/>
            <c:param name="password" value="${param.password}"/>
        </c:url><br>

        <%--属性处理 与 输出标签--%>
        <c:set var="login" value="zhongjingwen" scope="session"/>
        <c:set var="details" scope="session">
            value1,value2,value3
        </c:set>
        login = ${sessionScope.login} <br>

        <c:remove var="login" scope="session"/>
        login = ${sessionScope.login} <br>

        <%--    设置JavaBean的属性或Map对象的健/值，要使用target属性设置。
        <c:set target="${user}" property="name" value="${param.name}"/>
        --%>

        <%--自动用实体字符代替输出的角括号、单引号、双引号。由escapeXml属性控制，默认为true。default设置当value为null时的默认值--%>
        <c:out value="${param.message}" escapeXml="true" default="<\'><\">"/>
        <c:out value="${param.message}" escapeXml="false" default="<\'><\">"/>

    </body>
</html>
```

### I18N兼容标签库  
```jsp
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, javax.servlet.jsp.jstl.fmt.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>I18N兼容标签库</title>
    </head>
    <body>
        <%--信息标签--%>
        <fmt:setBundle basename="messages2"/><%-- 设置的作用域默认是对整个页面都有作用 --%>
        <fmt:message key="title"/><br>
        <fmt:message key="forUser">
            <fmt:param value="${param.name}"/>
            <fmt:param value="${now}"/>
            <fmt:param value="${now}"/>
        </fmt:message><br>
        <br>

        <fmt:bundle basename="messages"><%-- 作用域仅限body中--%>
            <fmt:message key="welcome"/>
            <fmt:message key="name"/><br>
        </fmt:bundle>
        <br>


        <%-- 地区标签 --%>
        <%
            Locale zhCN = new Locale("zh", "CN");
            Locale enUS = new Locale("en", "US");
            System.out.println(zhCN);
            System.out.println(enUS);

            ResourceBundle zh_CN = ResourceBundle.getBundle("messages", zhCN);
            ResourceBundle en_US = ResourceBundle.getBundle("messages", enUS);

            System.out.println(zh_CN.getLocale());
            System.out.println(en_US.getLocale());

            pageContext.setAttribute("zh_CN", new LocalizationContext(zh_CN));
            pageContext.setAttribute("en_US", new LocalizationContext(en_US));
        %>
        <fmt:message bundle="${zh_CN}" key="welcome"/><br>
        <fmt:message bundle="${en_US}" key="welcome"/><br>
            ${zh_CN.getLocale()}<br>
           ${en_US.getLocale()}<br>

        <fmt:setLocale value="zh_CN"/><%-- 共享Locale信息 --%>



        <%-- 格式标签 --%>

        <%-- 日期标签 --%>
            <%-- --%>
        <fmt:formatDate value="${now}"/><br>
        <%-- type 属性指定显示的内容（date, time, both）默认为date--%><br>
        <fmt:formatDate value="${now}" type = "date"/><br>
        <fmt:formatDate value="${now}" type = "time"/><br>
        <fmt:formatDate value="${now}" type = "both"/><br>
            <%-- dateStyle属性指定日期的详细程度 --%><br>
        <fmt:formatDate value="${now}" dateStyle="default"/><br>
        <fmt:formatDate value="${now}" dateStyle="short"/><br>
        <fmt:formatDate value="${now}" dateStyle="medium"/><br>
        <fmt:formatDate value="${now}" dateStyle="long"/><br>
        <fmt:formatDate value="${now}" dateStyle="full"/><br>
            <%-- timeStyle属性指定时间的详细程度 --%><br>
        <fmt:formatDate value="${now}" type = "time" timeStyle="default"/><br>
        <fmt:formatDate value="${now}" type = "time" timeStyle="short"/><br>
        <fmt:formatDate value="${now}" type = "time" timeStyle="medium"/><br>
        <fmt:formatDate value="${now}" type = "time" timeStyle="long"/><br>
        <fmt:formatDate value="${now}" type = "time"  timeStyle="full"/><br>
            <%-- pattern 属性自定义格式，与java.text.SimpleDateFormat的指定格式方式相同 --%><br>
        <fmt:formatDate value="${now}" pattern="yyyy-MM-dd hh:mm:ss"/><br>

        <%-- 时区标签 --%><br><br>
            <%-- 使用字符串或java.util.Timezone对象指定，字符串指定方式参考TimeZone的API文件说明 --%>
        <fmt:timeZone value="GMT+1:00">
            <fmt:formatDate value="${now}" type="both" dateStyle="full" timeStyle="full"/><br>
        </fmt:timeZone>
            <%-- 全局的时区设定，fmt:formatDate也有一个timeZone属性来设定时区 --%>
        <fmt:setTimeZone value="GMT+8:00"/>
        <fmt:formatDate timeZone="GMT+9:00" value="${now}" type="both" dateStyle="full" timeStyle="full"/>

        <%-- 数据格式化标签 --%><br>
            <%-- 根据不同地区设置呈现不同的格式 --%><br>
        <fmt:formatNumber value="12345.678"/><br>
            <%-- type属性，currency属性值指示货币格式化，precent属性值指示百分比封闭--%><br>
        <fmt:formatNumber value="12345.678" type="number"/><br>
        <fmt:formatNumber value="12345.678" type="currency"/><br>
        <fmt:formatNumber value="12345.678" type="percent"/><br>
            <%-- currencySymbol属性指定货币符号 --%><br>
        <fmt:formatNumber value="12345.678" type="currency" currencySymbol="币"/><br>
            <%-- pattern属性格式化，方式与java.text.DecimalFormat的说明相同 --%><br>
        <fmt:formatNumber value="12345.678" pattern="#,#00.0#"/><br>

        <%-- 解析标签 --%><br>
            <%-- fmt:parseDate与fmt:parseNumber 解析日期，将value属性上的数值解析按指定格式解析为原有的日期、时间或数字类型 --%><br>
       <fmt:parseDate value="2021-4-4 13:47:56" type="both" /><br>
       <fmt:parseNumber value="1,234,568%" type="percent"/><br>
       
    </body>
</html>
```

### XML标签库  
```jsp
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%-- 在Tomcat中，若要使用XML标签库，还必须使用Xalan-Java(xml.apache.org/xalan-j/index.html) --%>

<%-- 解析XML --%>
    <%-- 首先解析XML，var属性指定保存结果的属性名，doc属性指定文件来源，可以是字符串或Reader对象，scope属性默认保存结果到page属性范围 --%>
<c:import var="xml" url="bookmarks.xml" charEncoding="UTF-8"/>
<x:parse var="bookmarks" doc="${xml}" scope="page"/><%--  --%>
    <%-- 可以在body中放置XML解析 --%>
<x:parse var="bookmarks1">
    <bookmarks>
        <bookmark id="1">
            <title encoding="UTF-8">我的网站</title>
            <url>http://localhost:8080/contextPath_local-tomcat</url>
            <category>Servlet</category>
        </bookmark>
        <bookmark id="2">
            <title encoding="UTF-8">jstl</title>
            <url>http://localhost:8080/contextPath_local-tomcat/jstl</url>
            <category>JSTL</category>
        </bookmark>
    </bookmarks>
</x:parse>
<x:parse var="bookmarks2">
    <c:import url="bookmarks.xml" charEncoding="UTF-8"/>
</x:parse>

<%-- 输出 --%><br>
    <%-- 完成解析后，就可以用x:out输出,select属性以$开头，后跟解析结果存储时的属性名，最后是XPath表示式，默认从page范围取得解析结果 --%>
<x:out select="$bookmarks//bookmark[2]/title"/><br>
<x:out select="$pageScope:bookmarks//bookmark[2]/title"/><br><%-- Xpath隐式变量（使用EL隐式变量）绑定法，从指定属性范围取得解析结果 --%>
<x:out select="$bookmarks//bookmark[@id=$param:id]/title" /><br>

<%-- 保存 --%><br>
<x:set var="bookmarkst" select="$bookmarks//bookmark[2]/title" />
${bookmarkst}<br>

<%-- 流程处理标签哭 --%><br>
<x:if select="$bookmarks//bookmark[@id=$param:id]/title">
    <x:out select="$bookmarks//bookmark[@id=$param:id]/title"/><br>
</x:if><br>
<x:choose>
    <x:when select="$bookmarks//bookmark[@id=$param:id]/title">
        <x:out select="$bookmarks//bookmark[@id=$param:id]/title"/> <br>
    </x:when>
    <x:otherwise>
        指定的书签 id = ${param.id} 不存在<br>
    </x:otherwise>
</x:choose><br>
<x:forEach var="bookmark" select="$bookmarks//bookmark">
    <x:out select="$bookmark/title"/><br>
    <x:out select="$bookmark/url"/><br>
    <x:out select="$bookmark/category"/><br>
    <br>
</x:forEach>

<%-- 文件转换标签 --%><br>
<c:import var="xslt" url="bookmarks.xsl" charEncoding="UTF-8"/>
<x:transform doc="${xml}" xslt="${xslt}">
    <x:param name="headline" value="在线书签"/>
</x:transform>
```

### 函数标签库 
```jsp
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
    </head>
    <body>
        <%-- 改变大小写 --%><br>
        ${fn:toLowerCase(param.text)}<br>
        ${fn:toUpperCase(param.text)}<br>
        ------<br>
        <%-- 子串 --%>
        |${fn:substring(param.text, 1, 7)}|<br>
        |${fn:substringAfter(param.text, "ab")}|<br>
        |${fn:substringBefore(param.text, "AB")}|<br>
        ------<br>
        <%-- 裁剪空白 --%>
        |${fn:trim(param.text)}|<br>
        <%-- 检查是否含有子串 --%>
        ------<br>
        |${fn:startsWith(param.text, "a")}|<br>
        |${fn:endsWith(param.text, "b")}|<br>
        |${fn:contains(param.text, "ab")}|<br>
        |${fn:containsIgnoreCase(param.text, "Ab")}|<br>
        <%--检查子串位置--%>
        ------<br>
        |${fn:indexOf(param.text, "ab")}|<br>
        <%-- 切割字符串为字符串数组 --%>
        ------<br>
        |${a = fn:split(param.text, " ")}|<br>
        <c:forEach var="b" items="${a}">
            |${b}|<br>
        </c:forEach>
        <%-- 连接字符串数组为字符串 --%>
        ------<br>
        |${fn:join(a, " ")}|<br>
        <%-- 替换XML字符 --%>
        ------<br>
        |${fn:escapeXml(param.text)}|<br>

        <%-- 更多内容参考JSTL在线文件说明或JSTL规格数JSR52 --%>
    </body>
</html>
```
