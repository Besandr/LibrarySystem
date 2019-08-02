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
    <c:import url="header.jsp"/>

    <title><fmt:message key="adminControl.activeLoans"/> </title>
</head>
<body>
<div>
    <c:import url="adminControl.jsp"/>
</div>

<div><fmt:message key="adminControl.activeLoans"/>:</div>

<div>
    <table>
        <tr>
            <th>№</th>
            <th><fmt:message key="loans.bookTitle"/></th>
            <th><fmt:message key="loans.bookAuthors"/></th>
            <th><fmt:message key="loans.userName"/></th>
            <th><fmt:message key="loans.loanDate"/></th>
            <th><fmt:message key="loans.expiredDate"/></th>
        </tr>

        <c:forEach var="loanDTO" items="${activeLoans}">
            <tr>
                <td>${loanDTO.loan.id}</td>
                <td><a href="${contextPath}/admin/activeBookLoans?bookId=${loanDTO.book.id}">${loanDTO.book.title}</a></td>
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
                <td><a href="${contextPath}/admin/userProfile?userId=${loanDTO.user.id}">${loanDTO.user.lastName} ${loanDTO.user.firstName}</a></td>
                <td>${loanDTO.loan.loanDate}</td>
                <td>${loanDTO.loan.expiredDate}</td>
            </tr>
        </c:forEach>
    </table>
</div>

<%------------------------- Pagination part -------------------------------%>
<%--For displaying Previous link except for the 1st page --%>
<c:if test="${currentPage != 1}">
    <td><a href="${contextPath}/admin/activeLoans?page=${currentPage - 1}"><fmt:message key="pagination.previous"/> </a></td>
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
                    <td><a href="${contextPath}/admin/activeLoans?page=${i}">${i}</a></td>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </tr>
</table>

<%--For displaying Next link --%>
<c:if test="${currentPage lt pagesQuantity}">
    <td><a href="${contextPath}/admin/activeLoans?page=${currentPage + 1}"><fmt:message key="pagination.next"/> </a></td>
</c:if>

</body>
</html>