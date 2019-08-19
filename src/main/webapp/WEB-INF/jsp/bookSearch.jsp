<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty sessionScope.language}">
    <c:set var="language" value="${applicationScope.language}" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="textContent"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="bookSearch.title"/></title>
</head>
<body>

<div><c:import url="header.jsp"/></div>

<div class="d-flex justify-content-center">
    <h2><fmt:message key="bookSearch.title"/></h2>
</div>
<br>
<div class="d-flex justify-content-center">
    <form class="col-5" action="${contextPath}/bookSearch.do">
        <div class="row" style="margin:3%">
            <div class="col">
                <label for="authorSelect" class="col-12 col-form-label">
                    <fmt:message key="bookSearch.author"/>
                </label>
            </div>
            <div class="col">
                <select class="custom-select" id="authorSelect" name="authorId">
                    <option disabled selected value><fmt:message key="bookSearch.chooseAuthor"/></option>
                    <c:forEach var="author" items="${authors}">
                        <option value="${author.id}">${author.lastName} ${author.firstName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="row" style="margin:3%">
            <div class="col">
                <label for="bookTitleInput" class="col-12 col-form-label">
                    <fmt:message key="bookSearch.searchBookTitle"/>:
                </label>
            </div>
            <div class="col">
                <input class="form-control" id="bookTitleInput" name="bookTitle" type="text">
            </div>
        </div>
        <div class="row" style="margin:3%">
            <div class="col">
                <label for="keywordSelect" class="col-12 col-form-label">
                    <fmt:message key="keyword"/>:
                </label>
            </div>
            <div class="col">
                <select class="custom-select" id="keywordSelect" name="keywordId">
                    <option disabled selected value><fmt:message key="bookSearch.chooseKeyword"/></option>
                    <c:forEach var="keyword" items="${keywords}">
                        <option value="${keyword.id}">${keyword.word}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="d-flex justify-content-center" style="margin:3%">
            <button class="btn btn-lg brown-button"><fmt:message key="bookSearch.findBook"/></button>
        </div>
    </form>
</div>
<c:if test="${not empty books}">
    <div class="d-flex justify-content-center">
        <h3><fmt:message key="bookSearch.searchResult"/>:</h3>
    </div>
    <div class="bg-semi-transparent">
        <table class="table table-hover table-sm">

            <tr>
                <th class="text-center"><fmt:message key="bookSearch.result.action"/></th>
                <th class="text-center"><fmt:message key="bookSearch.result.author"/></th>
                <th class="text-center"><fmt:message key="bookSearch.result.title"/></th>
                <th class="text-center"><fmt:message key="bookSearch.result.year"/></th>
                <th class="text-center"><fmt:message key="bookSearch.result.description"/></th>
            </tr>

            <c:forEach var="bookDTO" items="${books}">
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${not empty loggedInUser && loggedInUser.role == 'ADMINISTRATOR'}">
                                <%--Store bookDto for management if user in Admin's role--%>
                                <form action="${contextPath}/admin/bookManagement" method="post">
                                    <input type="text" name="bookId" value="${bookDTO.book.id}" hidden>
                                    <button class="btn brown-button"><fmt:message key="bookSearch.result.choose"/></button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <%--Order book feature for ordinary user--%>
                                <form action="${contextPath}/user/orderBook.do" method="post">
                                    <input type="text" name="bookId" value="${bookDTO.book.id}" hidden>
                                    <button class="btn brown-button"><fmt:message key="bookSearch.result.order"/></button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td>
                        <div>
                            <ul style="list-style-type:none;">
                                <li>
                                    <c:forEach var="author" items="${bookDTO.authors}">
                                        ${author.firstName} ${author.lastName}
                                    </c:forEach>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <td>
                            ${bookDTO.book.title}
                    </td>
                    <td>
                            ${bookDTO.book.year}
                    </td>
                    <td>
                            ${bookDTO.book.description}
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <br>
        <%------------------------- Pagination part -------------------------------%>

    <nav>
        <ul class="pagination justify-content-center">
                <%--For displaying Previous link except for the 1st page --%>
            <c:choose>
                <c:when test="${currentPage != 1}">
                    <li class="page-item">
                        <a class="page-link text-dark"
                           href="${contextPath}/bookSearch.do?page=${currentPage - 1} "><fmt:message
                                key="pagination.previous"/> </a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item disabled">
                        <a class="page-link text-dark"
                           href="${contextPath}/bookSearch.do?page=${currentPage - 1}"><fmt:message
                                key="pagination.previous"/> </a>
                    </li>
                </c:otherwise>
            </c:choose>

                <%--For displaying Page numbers.
                The when condition does not display a link for the current page--%>

            <c:forEach begin="1" end="${pagesQuantity}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <li class="page-item">
                            <div class="page-link text-dark">${i}</div>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link text-dark"
                                                 href="${contextPath}/bookSearch.do?page=${i}">${i}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>


                <%--For displaying Next link --%>
            <c:choose>
                <c:when test="${currentPage lt pagesQuantity}">
                    <li class="page-item">
                        <a class="page-link text-dark"
                           href="${contextPath}/bookSearch.do?page=${currentPage + 1}"><fmt:message
                                key="pagination.next"/> </a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item disabled">
                        <a class="page-link text-dark"
                           href="${contextPath}/bookSearch.do?page=${currentPage + 1}"><fmt:message
                                key="pagination.next"/> </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</c:if>
</body>
</html>
