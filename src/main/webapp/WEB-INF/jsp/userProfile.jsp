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
    <title><fmt:message key="userProfile.title"/></title>
</head>

<body>
<div><c:import url="adminControl.jsp"/></div>


<div class="d-flex justify-content-center my-md-5">
    <h2><fmt:message key="userProfile.title"/></h2>
</div>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-md-auto">
        <table class="table table-borderless h5">
            <tbody>
                <tr>
                    <td><fmt:message key="firstName"/>:</td>
                    <td>${borrower.firstName}</td>
                </tr>
                <tr>
                    <td><fmt:message key="lastName"/>:</td>
                    <td>${borrower.lastName}</td>
                </tr>
                <tr>
                    <td><fmt:message key="email"/>:</td>
                    <td>${borrower.email}</td>
                </tr>
                <tr>
                    <td><fmt:message key="phone"/>:</td>
                    <td>${borrower.phone}</td>
                </tr>
            </tbody>
        </table>
        </div>
    </div>
</div>
</body>
</html>
