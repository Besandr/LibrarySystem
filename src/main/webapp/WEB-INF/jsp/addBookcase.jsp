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
    <c:import url="adminCabinet.jsp"/>

    <title><fmt:message key="bookcaseManagement.addingBookcase.title"/> </title>
</head>
<body>

<div><fmt:message key="bookcaseManagement.addingBookcase.header"/></div>
<br>
<c:if test="${errors.hasErrors}">
    <div style="color:red"><fmt:message key="bookcaseManagement.addingBookcase.errors"/></div>
</c:if>

<form action="${contextPath}/admin/bookcaseManagement/add">

    <c:if test="${errors.errorsMap['shelfQuantity'] != null}">
        <div><fmt:message key="${errors.errorsMap['shelfQuantity']}"/></div>
    </c:if>
    <label for="shelves"><fmt:message key="bookcaseManagement.addingBookcase.shelves"/> </label>
    <input id="shelves" type="text" name="shelfQuantity" value="${form.shelfQuantity}" required>

    <c:if test="${errors.errorsMap['cellQuantity'] != null}">
        <div><fmt:message key="${errors.errorsMap['cellQuantity']}"/></div>
    </c:if>
    <label for="cells"><fmt:message key="bookcaseManagement.addingBookcase.cells"/> </label>
    <input id="cells" type="text" name="cellQuantity" value="${form.cellQuantity}" required>

    <button><fmt:message key="bookcaseManagement.addingBookcase.submit"/></button>

</form>

</body>
</html>
