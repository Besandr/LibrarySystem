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

    <title><fmt:message key="registration.title"/></title>
</head>
<body>
<c:if test="${errors.hasErrors}">
    <p  style="color:red"><fmt:message key="registration.errors"/></p>
</c:if>
<c:if test="${errors.errorsMap['userError'] != null}">
    <fmt:message key="${errors.errorsMap['userError']}"/>
</c:if>

    <form action="register.do" method="post">

        <label>
            <h3><fmt:message key="registration.firstName"/></h3>
            <input name="firstName" type="text" value="${form.firstName}" required>
            <c:if test="${errors.errorsMap['firstName'] != null}">
                <fmt:message key="${errors.errorsMap['firstName']}"/>
            </c:if>
        </label>

        <label>
            <h3><fmt:message key="registration.lastName"/></h3>
            <input name="lastName" type="text" value="${form.lastName}" required>
            <c:if test="${errors.errorsMap['lastName'] != null}">
                <fmt:message key="${errors.errorsMap['lastName']}"/>
            </c:if>
        </label>

        <label>
            <h3><fmt:message key="email"/></h3>
            <input name="email" type="email" value="${form.email}" required>
            <c:if test="${errors.errorsMap['email'] != null}">
                <fmt:message key="${errors.errorsMap['email']}"/>
            </c:if>
        </label>

        <label>
            <h3><fmt:message key="phone"/></h3>
            <input name="phone" type="text" value="${form.phone}" required>
            <c:if test="${errors.errorsMap['phone'] != null}">
                <fmt:message key="${errors.errorsMap['phone']}"/>
            </c:if>
        </label>

        <label>
            <h3><fmt:message key="password"/></h3>
            <input name="password" type="password" value="${form.password}" required>
            <c:if test="${errors.errorsMap['password'] != null}">
                <fmt:message key="${errors.errorsMap['password']}"/>
            </c:if>
        </label>

        <label>
            <h3><fmt:message key="password.confirm"/></h3>
            <input name="confirmPassword" type="password" value="${form.confirmPassword}" required>
            <c:if test="${errors.errorsMap['confirmPassword'] != null}">
                <fmt:message key="${errors.errorsMap['confirmPassword']}"/>
            </c:if>
        </label>

        <button><fmt:message key="registration.submit"/></button>
    </form>
</body>
</html>
