<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty sessionScope.language}">
    <c:set var="language" value="${applicationScope.language}" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.language}" />
<fmt:setBundle basename="textContent" />

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html>
<head>
    <c:import url="header.jsp"/>

    <title><fmt:message key="accessDenied.title"/></title>
</head>
<body>

<div>
    <fmt:message key="accessDenied.deniedMessage"/>
</div>
<div>
    <a href="${contextPath}/title"><fmt:message key="header.home" /></a>
</div>

</body>
</html>
