<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty sessionScope.language}">
    <c:set var="language" value="${applicationScope.language}" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="textContent"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<link rel="stylesheet" href="${contextPath}/resources/css/bootstrap.css" type='text/css'>
<link rel="stylesheet" href="${contextPath}/resources/css/bootstrap-grid.css" type='text/css'>
<link rel="stylesheet" href="${contextPath}/resources/css/librarySystem.css" type='text/css'>
<script src="${contextPath}/resources/js/jquery-3.4.1.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>

<!--Navbar-->
<nav class="navbar navbar-expand-lg navbar-custom">

    <a class="navbar-brand" href="${contextPath}/title">
        <img src="${contextPath}/resources/img/books-icon.png" width="30" height="30" alt="">
    </a>

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <div class="navbar-nav mr-auto">

            <span class="navbar-text">
                ${sessionScope.loggedInUser.firstName} ${sessionScope.loggedInUser.lastName}
            </span>


            <a class="nav-item nav-link" href="${contextPath}/user/orderedBooks"><fmt:message
                    key="userCabinet.orderedBooks"/> </a>
            <span class="navbar-text">|</span>
            <a class="nav-item nav-link" href="${contextPath}/user/borrowedBooks"><fmt:message
                    key="userCabinet.borrowedBooks"/> </a>
            <span class="navbar-text">|</span>
            <a class="nav-item nav-link" href="${contextPath}/user/returnedBooks"><fmt:message
                    key="userCabinet.returnedBooks"/> </a>
            <span class="navbar-text">|</span>
            <a class="nav-item nav-link" href="${contextPath}/logout"
               onclick="return confirm('<fmt:message key="logout.confirm"/>')">
                <fmt:message key="logout.button"/>
            </a>

        </div>

        <div class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown"
               aria-haspopup="true" aria-expanded="false">
                ${language}
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                <c:forEach var="languageOption" items="${applicationScope.languages}">
                    <c:if test="${sessionScope.language ne languageOption.code}">
                        <a class="dropdown-item"
                           href="${contextPath}/changeLanguage?chosenLanguage=${languageOption}">${languageOption.name}</a>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>

</nav>



