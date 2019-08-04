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
    <title><fmt:message key="userProfile.title"/></title>
</head>
<body>

<div><c:import url="adminCabinet.jsp"/></div>

<div style="text-align-all: center">
    <div><fmt:message key="userProfile.title"/></div>

    <div>
        <div><fmt:message key="firstName"/>:</div>
        <div>${borrower.firstName}</div>
    </div>
    <br>
    <div>
        <div><fmt:message key="lastName"/>:</div>
        <div>${borrower.lastName}</div>
    </div>
    <br>
    <div>
        <div><fmt:message key="email"/>:</div>
        <div>${borrower.email}</div>
    </div>
    <br>
    <div>
        <div><fmt:message key="phone"/>:</div>
        <div>${borrower.phone}</div>
    </div>
</div>
</body>
</html>
