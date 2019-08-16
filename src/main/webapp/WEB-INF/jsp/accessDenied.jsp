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
    <title><fmt:message key="accessDenied.title"/></title>
</head>
<body>

<div><c:import url="header.jsp"/></div>

<div class="d-flex justify-content-center h-50 align-items-center">
    <h1 class="text-danger"><fmt:message key="accessDenied.deniedMessage"/></h1>
</div>
<div class="d-flex justify-content-center">
    <a class="btn btn-lg brown-button" href="${contextPath}/title"><fmt:message key="header.home" /></a>
</div>

</body>
</html>
