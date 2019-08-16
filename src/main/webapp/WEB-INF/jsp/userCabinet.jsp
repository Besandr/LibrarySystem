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

<c:if test="${not empty actionResult}">
    <div class="d-flex justify-content-center">
        <h3><fmt:message key="${actionResult}"/></h3>
    </div>
</c:if>

<br>
<div class="d-flex justify-content-center h-50 pt-md-5">
    <a class="btn btn-lg brown-button mx-md-5 my-auto" href="${contextPath}/user/orderedBooks" ><fmt:message key="userCabinet.orderedBooks"/> </a>
    <a class="btn btn-lg brown-button mx-md-5 my-auto" href="${contextPath}/user/borrowedBooks" ><fmt:message key="userCabinet.borrowedBooks"/> </a>
    <a class="btn btn-lg brown-button mx-md-5 my-auto" href="${contextPath}/user/returnedBooks" ><fmt:message key="userCabinet.returnedBooks"/> </a>
</div>

</body>
</html>
