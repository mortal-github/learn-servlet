<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="f" uri="http://tag/simpletag" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>自定义Simple Tag标签 </title>
    </head>
    <body>
        <f:if test="${param.password == '123456'}">
            私密数据<br>
        </f:if><br>

        ${a = [1,2,3,4,5,6,7,9]}<br>
        <f:forEach var="var" items="${a}">
            第${var}行<br>
        </f:forEach><br>

        ${names = ["zhongjingwen", "zhongDongXiao", "zhongjingCHAO"]}<br>
        <f:toUpperCase>
            <f:forEach var="name" items="${names}">
                ${name}<br>
            </f:forEach>
        </f:toUpperCase><br>

        <f:choose>
            <f:when test="${param.when1}">
                执行When1<br>
            </f:when>
            <f:when test="${param.when2}">
                执行When2<br>
            </f:when>
            <f:otherwise>
                执行Otherwise<br>
            </f:otherwise>
        </f:choose><br>
    </body>
</html>
