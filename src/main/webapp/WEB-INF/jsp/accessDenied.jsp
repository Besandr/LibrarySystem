<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <c:import url="header.jsp"/>
    <c:if test="${empty sessionScope.language}">
        <c:set var="language" value="${applicationScope.language}" scope="session"/>
    </c:if>
    <fmt:setLocale value="${sessionScope.language}" />
    <fmt:setBundle basename="textContent" />
    <title><fmt:message key="accessDenied.title"/></title>
</head>
<body>

<div>
    <fmt:message key="accessDenied.deniedMessage"/>
</div>
<div>
    <a href="title"><fmt:message key="header.home" /></a>
</div>

</body>
</html>
