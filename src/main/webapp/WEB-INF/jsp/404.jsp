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
    <title>404</title>
</head>
<body>

<div><c:import url="header.jsp"/></div>

<div class="d-flex justify-content-center my-md-5 display-1 text-danger">
    404
</div>

<div class="d-flex justify-content-center my-md-5">
    <h1><fmt:message key="message404"/> </h1>
</div>

<div class="d-flex justify-content-center my-md-5">
    <a href="${contextPath}/title" class="btn btn-lg brown-button">
        <fmt:message key="header.home" />
    </a>
</div>
</body>
</html>
