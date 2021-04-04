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