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
    <c:import url="adminCabinet.jsp"/>

    <title><fmt:message key="bookManagement.title"/></title>
</head>
<body>
<c:choose>
    <c:when test="${not empty bookDto}">
        <%--    Storing BookDto in request again for further operations--%>
        <c:set var="bookDto" scope="request" value="${bookDto}"/>
<%--        TODO TRANSLATE--%>
        <div>ВЫБРАННАЯ КНИГА:</div>
<%--        Book info--%>
        <div>
            <c:forEach var="author" items="${bookDto.authors}">
                <div>${author.firstName} ${author.lastName}</div>
            </c:forEach>
        </div>
        <br>
        <div>"${bookDto.book.title}"</div>
<%--        Showing managements actions--%>
        <a href=""><fmt:message key="bookManagement.addBooks"/></a>
        <a href=""><fmt:message key="bookManagement.removeBooks"/></a>
        <a href=""><fmt:message key="bookManagement.changeBookProperties"/></a>
        <a href=""><fmt:message key="bookManagement.deleteBook"/></a>
        <a href="${contextPath}/admin/activeBookLoans?bookId=${bookDto.book.id}"><fmt:message key="bookManagement.showBorrowers"/></a>

    </c:when>
    <c:otherwise>
<%--        todo TRANSLATE--%>
        <div>Создайте новую книгу или выберите существующую для дальнейших действий</div>
        <a href="${contextPath}/admin/createBook"><fmt:message key="bookManagement.createBook"/></a>
        <a href="${contextPath}/bookSearch"><fmt:message key="bookManagement.findBook"/></a>
    </c:otherwise>
</c:choose>


</body>
</html>
