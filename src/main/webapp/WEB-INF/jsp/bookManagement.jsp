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
    <title><fmt:message key="bookManagement.title"/></title>
</head>
<body>

<div><c:import url="adminCabinet.jsp"/></div>

<c:if test="${not empty actionResult}">
    <div>
        <fmt:message key="${actionResult}"/>
    </div>
</c:if>

<c:if test="${not empty bookDto}">
    <div><fmt:message key="bookManagement.chosenBook"/> </div>
<%--        Book info--%>
    <div>
        <c:forEach var="author" items="${bookDto.authors}">
            <div>${author.firstName} ${author.lastName}</div>
        </c:forEach>
    </div>
    <br>
    <div>"${bookDto.book.title}"</div>
<%--        Showing managements actions--%>
    <a href="${contextPath}/admin/bookManagement/addBooks.do"><fmt:message key="bookManagement.addBooks"/></a>
    <a href="${contextPath}/admin/bookManagement/removeBooks.do"><fmt:message key="bookManagement.removeBooks"/></a>
    <a href=""><fmt:message key="bookManagement.changeBookProperties"/></a>
    <a href="${contextPath}/admin/bookManagement/deleteBooks.do"><fmt:message key="bookManagement.deleteBook"/></a>
    <a href="${contextPath}/admin/activeBookLoans?bookId=${bookDto.book.id}"><fmt:message key="bookManagement.showBorrowers"/></a>

</c:if>

<%--Actions for another book--%>
<div><fmt:message key="bookManagement.findOrCreate"/> </div>
<a href="${contextPath}/admin/createBook"><fmt:message key="bookManagement.createBook"/></a>
<a href="${contextPath}/bookSearch"><fmt:message key="bookManagement.findBook"/></a>


</body>
</html>
