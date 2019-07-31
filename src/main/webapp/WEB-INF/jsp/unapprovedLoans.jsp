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
<div>
    <table>
        <tr>
            <th>№</th>
            <th><fmt:message key="unapprovedLoans.bookTitle"/></th>
            <th><fmt:message key="unapprovedLoans.bookAuthors"/></th>
            <th><fmt:message key="unapprovedLoans.userName"/></th>
            <th><fmt:message key="unapprovedLoans.userKarma"/></th>
            <th><fmt:message key="unapprovedLoans.applicationDate"/></th>
            <th><fmt:message key="unapprovedLoans.availableBooks"/></th>
            <th><fmt:message key="unapprovedLoans.approvement"/></th>
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
                <td>${loanDTO.user.lastName} ${loanDTO.user.firstName}</td>
                <td>${loanDTO.user.karma}</td>
                <td>${loanDTO.loan.applyDate}</td>
                <td>${loanDTO.bookQuantity}</td>
                <td>
                    <form action="${contextPath}/admin/approve.do">
                        <input type="text" value="${loanDTO.loan.id}" hidden>
                        <button><fmt:message key="unapprovedLoans.approve"/> </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<c:import url="pagination.jsp"/>

</body>
</html>
