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

    <title><fmt:message key="adminControl.activeBookLoans"/> </title>
</head>
<body>
<div>
    <c:import url="adminControl.jsp"/>
</div>

<div>
    <p><fmt:message key="activeBookLoans.tableTitle"/></p>
    <p>
        <c:forEach var="author" items="${bookLoans[0].authors}">
            ${author.firstName} ${author.lastName}
        </c:forEach>
        <br>
        "${bookLoans[0].book.title}"
    </p>
</div>

<div>
    <table>
        <tr>
            <th><fmt:message key="loans.loanNumber"/></th>
            <th><fmt:message key="loans.userName"/></th>
            <th><fmt:message key="loans.loanDate"/></th>
        </tr>

        <c:forEach var="loanDTO" items="${bookLoans}">
            <tr>
                <td>${loanDTO.loan.id}</td>
                <td><a href="${contextPath}/admin/userProfile?userId=${loanDTO.user.id}">${loanDTO.user.lastName} ${loanDTO.user.firstName}</a></td>
                <td>${loanDTO.loan.loanDate}</td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
