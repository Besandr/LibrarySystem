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
    <title><fmt:message key="bookSearch.title"/></title>
</head>
<body>

<div><c:import url="header.jsp"/></div>

<div>
    <h2><fmt:message key="bookSearch.title"/></h2>
</div>
<br>
<div>
    <form action="${contextPath}/bookSearch.do">

        <label>
            <fmt:message key="bookSearch.author"/>
            <select name="authorId">
                <option disabled selected value><fmt:message key="bookSearch.chooseAuthor"/></option>
                <c:forEach var="author" items="${authors}">
                    <option value="${author.id}">${author.lastName} ${author.firstName}</option>
                </c:forEach>
            </select>
        </label>
        <br>
        <label>
            <fmt:message key="bookSearch.searchBookTitle"/>:
            <input name="bookTitle" type="text">
        </label>
        <br>
        <label>
            <fmt:message key="bookSearch.keyword"/>:
            <select name="keywordId">
                <option disabled selected value><fmt:message key="bookSearch.chooseKeyword"/></option>
                <c:forEach var="keyword" items="${keywords}">
                    <option value="${keyword.id}">${keyword.word}</option>
                </c:forEach>
            </select>
        </label>
        <br>
        <button><fmt:message key="bookSearch.findBook"/> </button>
    </form>
</div>
<c:if test="${not empty books}">
    <hr>
    <div>
        <fmt:message key="bookSearch.searchResult"/>:
    </div>
    <div>
        <table>

            <tr>
                <th><fmt:message key="bookSearch.result.action"/></th>
                <th><fmt:message key="bookSearch.result.author"/></th>
                <th><fmt:message key="bookSearch.result.title"/></th>
                <th><fmt:message key="bookSearch.result.year"/></th>
                <th><fmt:message key="bookSearch.result.description"/></th>
            </tr>

            <c:forEach var="bookDTO" items="${books}">
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${not empty loggedInUser && loggedInUser.role == 'ADMINISTRATOR'}">
<%--                                Store bookDto for management if user in Admin's role--%>
                                <form action="${contextPath}/admin/bookManagement" method="post">
                                    <input type="text" name="bookId" value="${bookDTO.book.id}" hidden>
                                    <button><fmt:message key="bookSearch.result.choose"/></button>
                                </form>
                            </c:when>
                            <c:otherwise>
<%--                                Order book feature for ordinary user--%>
                                <form action="${contextPath}/user/orderBook.do" method="post">
                                <input type="text" name="bookId" value="${bookDTO.book.id}" hidden>
                                <button><fmt:message key="bookSearch.result.order"/></button>
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
                        </div></td>
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
    <%--For displaying Previous link except for the 1st page --%>
    <c:if test="${currentPage != 1}">
        <td><a href="${contextPath}/bookSearch.do?page=${currentPage - 1}"><fmt:message key="pagination.previous"/> </a></td>
    </c:if>

    <%--For displaying Page numbers.
    The when condition does not display a link for the current page--%>
    <table border="1" cellpadding="5" cellspacing="5">
        <tr>
            <c:forEach begin="1" end="${pagesQuantity}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td><a href="${contextPath}/bookSearch.do?page=${i}">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </table>

    <%--For displaying Next link --%>
    <c:if test="${currentPage lt pagesQuantity}">
        <td><a href="${contextPath}/bookSearch.do?page=${currentPage + 1}"><fmt:message key="pagination.next"/> </a></td>
    </c:if>
</c:if>

<%--Printing book ordering result--%>
<c:if test="${not empty orderResult}">
    <div>
        <fmt:message key="${orderResult}"/>
    </div>
</c:if>
</body>
</html>
