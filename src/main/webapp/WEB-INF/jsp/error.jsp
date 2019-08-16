<%@ page contentType="text/html;charset=UTF-8" %>
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
    <title><fmt:message key="error.title"/> </title>
</head>
<body>

<div><c:import url="header.jsp"/></div>
<div class="d-flex justify-content-center h-50 align-items-center">
    <h1><fmt:message key="error.message"/> </h1>
</div>
<div class="d-flex justify-content-center">
    <a href="${contextPath}/title" class="btn btn-lg brown-button">Continue</a>
</div>
</body>
</html>
