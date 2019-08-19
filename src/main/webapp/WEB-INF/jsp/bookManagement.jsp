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

<div><c:import url="adminControl.jsp"/></div>


<c:if test="${not empty actionResult}">
    <div class="d-flex justify-content-center">
        <h2><fmt:message key="${actionResult}"/></h2>
    </div>
</c:if>

<c:if test="${not empty bookDto}">
    <div class="d-flex justify-content-center my-md-5">
        <h4><fmt:message key="bookManagement.chosenBook"/></h4>
    </div>
    <%--Book info--%>
    <div class="d-flex justify-content-center">
        <c:forEach var="author" items="${bookDto.authors}">
            <div class="ml-3">${author.firstName} ${author.lastName}</div>
        </c:forEach>
    </div>
    <br>
    <div class="d-flex justify-content-center">"${bookDto.book.title}"</div>
    <div class="d-flex justify-content-center">"${bookDto.book.year}"</div>
    <div class="d-flex justify-content-center">
        <c:forEach var="keyword" items="${bookDto.keywords}">
            <div class="ml-3">${keyword.word}</div>
        </c:forEach>
    </div>
    <%--Showing managements actions--%>
    <div class="d-flex justify-content-center h-20 pt-md-5">
        <a class="btn btn-lg brown-button mx-md-1 my-auto" href="${contextPath}/admin/bookManagement/addBooks"><fmt:message key="bookManagement.addBooks"/></a>
        <a class="btn btn-lg brown-button mx-md-1 my-auto" href="${contextPath}/admin/bookManagement/removeBooks.do"><fmt:message key="bookManagement.removeBooks"/></a>
        <a class="btn btn-lg brown-button mx-md-1 my-auto" href="${contextPath}/admin/bookManagement/updateBook"><fmt:message key="bookManagement.changeBookProperties"/></a>
        <a class="btn btn-lg brown-button mx-md-1 my-auto" href="${contextPath}/admin/bookManagement/deleteBooks.do"><fmt:message key="bookManagement.deleteBook"/></a>
        <a class="btn btn-lg brown-button mx-md-1 my-auto" href="${contextPath}/admin/activeBookLoans?bookId=${bookDto.book.id}"><fmt:message key="bookManagement.showBorrowers"/></a>
    </div>

</c:if>

<%--Actions for another book--%>
<div class="d-flex justify-content-center my-md-5">
    <h3><fmt:message key="bookManagement.findOrCreate"/></h3>
</div>
<div class="d-flex justify-content-center h-20 pt-md-1">
    <a class="btn btn-lg brown-button mx-md-1 my-auto" href="${contextPath}/admin/bookManagement/createBook"><fmt:message key="bookManagement.createBook"/></a>
    <a class="btn btn-lg brown-button mx-md-1 my-auto" href="${contextPath}/bookSearch"><fmt:message key="bookManagement.findBook"/></a>
</div>

</body>
</html>
