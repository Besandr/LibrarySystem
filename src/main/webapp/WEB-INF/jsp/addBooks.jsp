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

    <title><fmt:message key="bookManagement.addBook.title"/></title>
</head>
<body>

<div><fmt:message key="bookManagement.addBook.header"/> </div>

<c:choose>
    <c:when test="${not empty bookDto}">
        <div><fmt:message key="bookManagement.chosenBook"/> </div>
        <%--        Book info--%>
        <div>
            <c:forEach var="author" items="${bookDto.authors}">
                <div>${author.firstName} ${author.lastName}</div>
            </c:forEach>
        </div>
        <br>
        <div>"${bookDto.book.title}"</div>

<%--        Books adding form--%>
        <form action="${contextPath}/admin/bookManagement/addBooks.do">
            <label for="quantityInput"><fmt:message key="bookManagement.quantity"/> </label>
            <input id="quantityInput" type="text" name="booksQuantity" required>
            <button><fmt:message key="bookManagement.addBooks"/> </button>
        </form>

    </c:when>
    <c:otherwise>
        <div><fmt:message key="bookManagement.findOrCreate"/> </div>
        <a href="${contextPath}/admin/createBook"><fmt:message key="bookManagement.createBook"/></a>
        <a href="${contextPath}/bookSearch"><fmt:message key="bookManagement.findBook"/></a>
    </c:otherwise>
</c:choose>


</body>
</html>
