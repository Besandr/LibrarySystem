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
    <title><fmt:message key="userCabinet.orderedBooks"/> </title>
</head>
<body>

<div><c:import url="userControl.jsp"/></div>

<div>
    <table>
        <tr>
            <th><fmt:message key="loans.bookTitle"/></th>
            <th><fmt:message key="loans.bookAuthors"/></th>
            <th><fmt:message key="loans.applicationDate"/></th>
        </tr>

        <c:forEach var="loanDTO" items="${loans}">
            <tr>
                <td>${loanDTO.book.title}</td>
                <td>
                    <div>
                        <ul style="list-style-type:none;">
                            <li>
                                <c:forEach var="author" items="${loanDTO.authors}">
                                    ${author.firstName} ${author.lastName}
                                </c:forEach>
                            </li>
                        </ul>
                    </div>
                </td>
                <td>${loanDTO.loan.applyDate}</td>
            </tr>
        </c:forEach>
    </table>
</div>

<%------------------------- Pagination part -------------------------------%>
<%--For displaying Previous link except for the 1st page --%>
<c:if test="${currentPage != 1}">
    <td><a href="${contextPath}/admin/orderedBooks?page=${currentPage - 1}"><fmt:message key="pagination.previous"/> </a></td>
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
                    <td><a href="${contextPath}/admin/orderedBooks?page=${i}">${i}</a></td>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </tr>
</table>

<%--For displaying Next link --%>
<c:if test="${currentPage lt pagesQuantity}">
    <td><a href="${contextPath}/admin/orderedBooks?page=${currentPage + 1}"><fmt:message key="pagination.next"/> </a></td>
</c:if>

</body>
</html>
