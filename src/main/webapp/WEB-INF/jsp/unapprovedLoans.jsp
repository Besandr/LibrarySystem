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

    <title><fmt:message key="adminControl.unapprovedLoans"/> </title>
</head>
<body>
<div>
    <c:import url="adminControl.jsp"/>
</div>

<%--Prints loan approving information if exists--%>
<c:if test="${not empty approvingResult}">
    <c:choose>
        <c:when test="${approvingResult == true}">
            <fmt:message key="loans.approvingSuccessful"/> ${loanId}
        </c:when>
        <c:otherwise>
            <fmt:message key="loans.approvingFailed"/> ${loanId}
        </c:otherwise>
    </c:choose>
</c:if>

<div>
    <table>
        <tr>
            <th>№</th>
            <th><fmt:message key="loans.bookTitle"/></th>
            <th><fmt:message key="loans.bookAuthors"/></th>
            <th><fmt:message key="loans.userName"/></th>
            <th><fmt:message key="loans.userKarma"/></th>
            <th><fmt:message key="loans.applicationDate"/></th>
            <th><fmt:message key="loans.availableBooks"/></th>
            <th><fmt:message key="loans.approvement"/></th>
        </tr>

        <c:forEach var="loanDTO" items="${loans}">
            <tr>
                <td>${loanDTO.loan.id}</td>
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
                <td><a href="${contextPath}/admin/userProfile?userId=${loanDTO.user.id}">${loanDTO.user.lastName} ${loanDTO.user.firstName}</a></td>
                <td>${loanDTO.user.karma}</td>
                <td>${loanDTO.loan.applyDate}</td>
                <td>${loanDTO.bookQuantity}</td>
                <td>
                    <form action="${contextPath}/admin/approve.do">
                        <input type="text" name="loanId" value="${loanDTO.loan.id}" hidden>
                        <button><fmt:message key="loans.approve"/> </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<%------------------------- Pagination part -------------------------------%>
<%--For displaying Previous link except for the 1st page --%>
<c:if test="${currentPage != 1}">
    <td><a href="${contextPath}/admin/unapprovedLoans?page=${currentPage - 1}"><fmt:message key="pagination.previous"/> </a></td>
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
                    <td><a href="${contextPath}/admin/unapprovedLoans?page=${i}">${i}</a></td>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </tr>
</table>

<%--For displaying Next link --%>
<c:if test="${currentPage lt pagesQuantity}">
    <td><a href="${contextPath}/admin/unapprovedLoans?page=${currentPage + 1}"><fmt:message key="pagination.next"/> </a></td>
</c:if>

</body>
</html>
