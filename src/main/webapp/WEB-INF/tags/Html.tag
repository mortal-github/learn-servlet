<%@ tag description="header 内容" pageEncoding="UTF-8"
body-content="scriptless"%><%-- body-content默认scriptless即不允许Body有Scriptlet,empty表示一定没有body内容，tagdependent表示将Body的内容当作纯文字处理 --%>
<%@ attribute name="title" %>
<!DOCTYPE html>

<head>
    <meta charset="UTF-8"/>
    <title>${title}</title>
</head>
<body>
    <jsp:doBody/>
</body>
</html>