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
    <title><fmt:message key="userCabinet.title"/></title>
</head>
<body>

<div><c:import url="header.jsp"/></div>

<br>
<a href="${contextPath}/user/orderedBooks" ><fmt:message key="userCabinet.orderedBooks"/> </a>
<a href="" ><fmt:message key="userCabinet.borrowedBooks"/> </a>


</body>
</html>
