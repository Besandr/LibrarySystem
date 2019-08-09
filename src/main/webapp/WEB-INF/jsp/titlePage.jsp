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
    <link rel="stylesheet" href="${contextPath}/resources/css/librarySystem.css" type='text/css'>
    <link rel="stylesheet" href="${contextPath}/resources/css/bootstrap.css" type='text/css'>
    <link rel="stylesheet" href="${contextPath}/resources/css/bootstrap-grid.css" type='text/css'>

    <title><fmt:message key="title.title"/></title>
</head>
<body class="title-body">

<div><c:import url="header.jsp"/></div>

<br>
<div class="container d-flex h-100 align-items-center">
    <div class="mx-auto text-center">
        <a class="btn btn-warning btn-lg find-book-link" href="${contextPath}/bookSearch"><fmt:message key="title.findBooks"/> </a>
    </div>
</div>
</body>
</html>
