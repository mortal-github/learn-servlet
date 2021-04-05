<%@ taglib prefix="html" tagdir="/WEB-INF/tags" %><%-- 定义前置与Tag File位置，实际上只能指定/WEB-INF/tags --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
    </head>
    <body>
        <c:catch var="error">
            ${param.a} + ${param.b} = ${param.a + param.b}
        </c:catch>
        <c:set var="errors" value="${error}" scope="request"/>
        <html:Errors/>

    </body>
</html>