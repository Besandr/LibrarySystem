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
    <title><fmt:message key="adminControl.activeLoans"/> </title>
</head>
<body>

<div><c:import url="adminControl.jsp"/></div>

<div class="d-flex justify-content-center my-md-5">
    <h1><fmt:message key="adminControl.activeLoans"/></h1>
</div>

<c:choose>
    <c:when test="${not empty activeLoans}">
        <div class="bg-semi-transparent mx-md-5 d-flex justify-content-center">
            <table class="table table-hover table-md">
                <tr>
                    <th class="text-center">#</th>
                    <th><fmt:message key="loans.bookTitle"/></th>
                    <th class="text-center"><fmt:message key="loans.bookAuthors"/></th>
                    <th><fmt:message key="loans.userName"/></th>
                    <th class="text-center"><fmt:message key="loans.loanDate"/></th>
                    <th class="text-center"><fmt:message key="loans.expiredDate"/></th>
                </tr>

                <c:forEach var="loanDTO" items="${activeLoans}">
                    <tr>
                        <td class="text-center">${loanDTO.loan.id}</td>
                        <td><a href="${contextPath}/admin/activeBookLoans?bookId=${loanDTO.book.id}">${loanDTO.book.title}</a></td>
                        <td class="text-center">
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
                        <td class="text-center">${loanDTO.loan.loanDate}</td>
                        <td class="text-center">${loanDTO.loan.expiredDate}</td>
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
                               href="${contextPath}/admin/activeLoans?page=${currentPage - 1} "><fmt:message
                                    key="pagination.previous"/> </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item disabled">
                            <a class="page-link text-dark"
                               href="${contextPath}/admin/activeLoans?page=${currentPage - 1}"><fmt:message
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
                                                     href="${contextPath}/admin/activeLoans?page=${i}">${i}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>


                    <%--For displaying Next link --%>
                <c:choose>
                    <c:when test="${currentPage lt pagesQuantity}">
                        <li class="page-item">
                            <a class="page-link text-dark"
                               href="${contextPath}/admin/activeLoans?page=${currentPage + 1}"><fmt:message
                                    key="pagination.next"/> </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item disabled">
                            <a class="page-link text-dark"
                               href="${contextPath}/admin/activeLoans?page=${currentPage + 1}"><fmt:message
                                    key="pagination.next"/> </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </c:when>
    <c:otherwise>
        <div class="d-flex justify-content-center">
            <h3><fmt:message key="noData"/></h3>
        </div>
    </c:otherwise>
</c:choose>

</body>
</html>
