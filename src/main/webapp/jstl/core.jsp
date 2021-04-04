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