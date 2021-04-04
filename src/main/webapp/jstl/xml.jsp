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