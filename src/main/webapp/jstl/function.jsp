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