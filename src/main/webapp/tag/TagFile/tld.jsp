<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="html" uri="http://tlds/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html:Html title="TLD 文件">
    <c:catch var="error">
        ${param.a + param.b}
    </c:catch>
    <c:set var="errors" value="${error}" scope="request"/>
    <html:Errors/>
</html:Html>