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
    <title><fmt:message key="bookManagement.addBook.title"/></title>
</head>
<body>

<div><c:import url="adminControl.jsp"/></div>

<div class="d-flex justify-content-center my-md-5">
    <h3><fmt:message key="bookManagement.addBook.header"/></h3>
</div>

<c:choose>
    <c:when test="${not empty bookDto}">
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
                <div>${keyword.word}</div>
            </c:forEach>
        </div>

        <%--Books adding form--%>
        <div class="d-flex justify-content-center align-items-center my-5">
            <form action="${contextPath}/admin/bookManagement/addBooks.do">
                <div class="form-row justify-content-center">
                    <label for="quantityInput">
                     <h4><fmt:message key="bookManagement.quantity"/></h4>
                    </label>
                </div>
                <div class="form-row justify-content-center my-3">
                    <c:choose>
                        <c:when test="${errors.errorsMap['booksQuantity'] != null}">
                            <input id="quantityInput" class="form-control is-invalid" type="text" name="booksQuantity" required>
                            <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                                <fmt:message key="${errors.errorsMap['booksQuantity']}"/>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <input id="quantityInput" type="text" name="booksQuantity" required>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="form-row justify-content-center">
                    <button class="btn btn-lg brown-button"><fmt:message key="bookManagement.addBooks"/> </button>
                </div>
            </form>
        </div>

    </c:when>
    <c:otherwise>
        <%--Actions for another book--%>
        <div class="d-flex justify-content-center my-md-5">
            <h3><fmt:message key="bookManagement.findOrCreate"/></h3>
        </div>
        <div class="d-flex justify-content-center h-20 pt-md-1">
            <a class="btn btn-lg brown-button mx-md-1 my-auto" href="${contextPath}/admin/bookManagement/createBook"><fmt:message key="bookManagement.createBook"/></a>
            <a class="btn btn-lg brown-button mx-md-1 my-auto" href="${contextPath}/bookSearch"><fmt:message key="bookManagement.findBook"/></a>
        </div>
    </c:otherwise>
</c:choose>


</body>
</html>
