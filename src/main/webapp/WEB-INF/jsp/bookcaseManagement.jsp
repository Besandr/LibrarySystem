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
    <title><fmt:message key="bookcaseManagement.title"/> </title>
</head>
<body>

<div><c:import url="adminCabinet.jsp"/></div>

<c:if test="${not empty actionResult}">
    <div>
        <fmt:message key="${actionResult}"/>
    </div>
</c:if>

<div><fmt:message key="bookcaseManagement.header"/></div>
<%--        Showing managements actions--%>
<a href="${contextPath}/admin/bookcaseManagement/add"><fmt:message key="bookcaseManagement.addingBookcase.title"/></a>

</body>
</html>
