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
    <title><fmt:message key="login.title"/></title>
</head>
<body>

<div><c:import url="header.jsp"/></div>

<div>

    <c:if test="${errors.hasErrors}">
        <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
            <h3><fmt:message key="login.error.noSuchUser"/></h3>
        </div>
    </c:if>

    <%--    Show message to user redirected to the login page--%>
    <c:if test="${not empty needAuthentication}">
        <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
            <h3><fmt:message key="login.needAuthentication"/></h3>
        </div>
    </c:if>


    <div class="d-flex justify-content-center">

        <form action="${contextPath}/login.do" method="post">
            <div class="form-row">
                <label for="userEmail">
                    <h3><fmt:message key="email"/></h3>
                </label>
                <c:choose>
                    <c:when test="${errors.errorsMap['email'] != null}">
                        <input class="form-control is-invalid" id="userEmail" name="email" type="text"
                               value="${form.email}" required autofocus>
                        <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                            <fmt:message key="${errors.errorsMap['email']}"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <input class="form-control" id="userEmail" name="email" type="text" value="${form.email}"
                               required autofocus>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="form-row">
                <label for="userPassword">
                    <h3><fmt:message key="password"/></h3>
                </label>
                <c:choose>
                    <c:when test="${errors.errorsMap['password'] != null}">
                        <input class="form-control is-invalid" id="userPassword" name="password" type="password"
                               value="${form.password}" required>
                        <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                            <fmt:message key="${errors.errorsMap['password']}"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <input class="form-control" id="userPassword" name="password" type="password"
                               value="${form.password}" required>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="d-flex justify-content-center">
                <button class="btn brown-button btn-lg" style="margin:3%"><fmt:message key="header.login"/></button>
            </div>

        </form>
    </div>
    <div class="d-flex justify-content-center">
        <a class="btn btn-warning btn-lg" href="${contextPath}/registration"><fmt:message key="login.registration"/></a>
    </div>

</div>


</body>
</html>
