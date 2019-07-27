<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <c:import url="header.jsp"/>
    <c:if test="${empty sessionScope.language}">
        <c:set var="language" value="${applicationScope.language}" scope="session"/>
    </c:if>
    <fmt:setLocale value="${sessionScope.language}" />
    <fmt:setBundle basename="textContent" />
    <title><fmt:message key="login.title"/></title>
</head>
<body>
<div>
    <c:if test="${errors.hasErrors}">
        <p  style="color:red"><fmt:message key="login.error.noSuchUser"/></p>
    </c:if>
    <form action="login.do" method="post">
        <label>
            <h3><fmt:message key="email"/></h3>
            <input name="email" type="text" value="${form.email}" required>
            <c:if test="${errors.errorsMap['email'] != null}">
                <fmt:message key="${errors.errorsMap['email']}"/>
            </c:if>
        </label>
        <h3><fmt:message key="password"/></h3>
        <label>
            <input name="password" type="password" value="${form.password}" required>
            <c:if test="${errors.errorsMap['password'] != null}">
                <fmt:message key="login.error.password"/>
            </c:if>
        </label>
        <button><fmt:message key="header.login"/></button>
    </form>
</div>
</body>
</html>
