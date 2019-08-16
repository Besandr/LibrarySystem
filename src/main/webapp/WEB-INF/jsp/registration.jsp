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
    <title><fmt:message key="registration.title"/></title>
</head>
<body>

<div><c:import url="header.jsp"/></div>

<c:choose>
    <c:when test="${errors.hasErrors}">
        <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
            <h3><fmt:message key="registration.errors"/></h3>
        </div>
        <c:if test="${errors.errorsMap['userError'] != null}">
            <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                <h3><fmt:message key="${errors.errorsMap['userError']}"/></h3>
            </div>
        </c:if>
        <%---------------------Form with errors----------------------%>
        <div class="d-flex justify-content-center mt-5">
            <form action="${contextPath}/register.do" method="post">

                <div class="form-row">
                    <div class="col">
                        <label for="firstNameInput">
                            <h3><fmt:message key="firstName"/></h3>
                        </label>
                    </div>
                    <div class="col">
                        <c:choose>
                            <c:when test="${errors.errorsMap['firstName'] != null}">
                                <input class="form-control is-invalid" name="firstName" type="text" value="${form.firstName}" placeholder="<fmt:message key="firstName"/>" required>
                                <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                                    <fmt:message key="${errors.errorsMap['firstName']}"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <input class="form-control is-valid" name="firstName" type="text" value="${form.firstName}" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-row">
                    <div class="col">
                        <label for="lastNameInput">
                            <h3><fmt:message key="lastName"/></h3>
                        </label>
                    </div>
                    <div class="col">
                        <c:choose>
                            <c:when test="${errors.errorsMap['lastName'] != null}">
                                <input class="form-control is-invalid" name="lastName" type="text" value="${form.lastName}" placeholder="<fmt:message key="lastName"/>" required>
                                <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                                    <fmt:message key="${errors.errorsMap['lastName']}"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <input class="form-control is-valid" name="lastName" type="text" value="${form.lastName}" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-row">
                    <div class="col">
                        <label for="emailInput">
                            <h3><fmt:message key="email"/></h3>
                        </label>
                    </div>
                    <div class="col">
                        <c:choose>
                            <c:when test="${errors.errorsMap['email'] != null}">
                                <input class="form-control is-invalid" name="email" type="email" value="${form.email}" placeholder="<fmt:message key="email"/>" required>
                                <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                                    <fmt:message key="${errors.errorsMap['email']}"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <input class="form-control is-valid" name="email" type="email" value="${form.email}" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-row">
                    <div class="col">
                        <label for="phoneInput">
                            <h3><fmt:message key="phone"/></h3>
                        </label>
                    </div>
                    <div class="col">
                        <c:choose>
                            <c:when test="${errors.errorsMap['phone'] != null}">
                                <input class="form-control is-invalid" name="phone" type="text" value="${form.phone}" placeholder="<fmt:message key="phone"/>" required>
                                <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                                    <fmt:message key="${errors.errorsMap['phone']}"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <input class="form-control is-valid" name="phone" type="text" value="${form.phone}" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-row">
                    <div class="col">
                        <label for="passwordInput">
                            <h3><fmt:message key="password"/></h3>
                        </label>
                    </div>
                    <div class="col">
                        <c:choose>
                            <c:when test="${errors.errorsMap['password'] != null}">
                                <input class="form-control is-invalid" name="password" type="password" value="${form.password}" placeholder="<fmt:message key="password"/>" required>
                                <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                                    <fmt:message key="${errors.errorsMap['password']}"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <input class="form-control is-valid" name="password" type="password" value="${form.password}" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-row align-content-center">
                    <div class="col">
                        <label for="confirmInput">
                            <h3><fmt:message key="password.confirm"/></h3>
                        </label>
                    </div>
                    <div class="col py-4">
                        <c:choose>
                            <c:when test="${errors.errorsMap['confirmPassword'] != null}">
                                <input class="form-control is-invalid" name="confirmPassword" type="password" value="${form.confirmPassword}" placeholder="<fmt:message key="password.confirm"/>" required>
                                <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                                    <fmt:message key="${errors.errorsMap['confirmPassword']}"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <input class="form-control is-valid" name="confirmPassword" type="password" value="${form.confirmPassword}" required>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="d-flex justify-content-center">
                    <button class="btn brown-button btn-lg"><fmt:message key="registration.submit"/></button>
                </div>
            </form>
        </div>
    </c:when>

    <c:otherwise>
        <%---------------------Form without errors----------------------%>
    <div class="d-flex justify-content-center mt-5">
        <form action="${contextPath}/register.do" method="post">
            <div class="form-row">
                <div class="col">
                    <label for="firstNameInput">
                        <h3><fmt:message key="firstName"/></h3>
                    </label>
                </div>
                <div class="col">
                    <input id="firstNameInput" name="firstName" type="text" value="${form.firstName}" placeholder="<fmt:message key="firstName"/>" required>
                </div>
            </div>

            <div class="form-row">
                <div class="col">
                    <label for="lastNameInput">
                        <h3><fmt:message key="lastName"/></h3>
                    </label>
                </div>
                <div class="col">
                 <input id="lastNameInput" name="lastName" type="text" value="${form.lastName}" placeholder="<fmt:message key="lastName"/>" required>
                </div>
            </div>

            <div class="form-row">
                <div class="col">
                    <label for="emailInput">
                        <h3><fmt:message key="email"/></h3>
                    </label>
                </div>
                <div class="col">
                 <input id="emailInput" name="email" type="email" value="${form.email}" placeholder="<fmt:message key="email"/>" required>
                </div>
            </div>

            <div class="form-row">
                <div class="col">
                    <label for="phoneInput">
                        <h3><fmt:message key="phone"/></h3>
                    </label>
                </div>
                <div class="col">
                   <input id="phoneInput" name="phone" type="text" value="${form.phone}" placeholder="<fmt:message key="phone"/>" required>
                </div>
            </div>

            <div class="form-row">
                <div class="col">
                    <label for="passwordInput">
                        <h3><fmt:message key="password"/></h3>
                    </label>
                </div>
                <div class="col">
                  <input id="passwordInput" name="password" type="password" value="${form.password}" placeholder="<fmt:message key="password"/>" required>
                </div>
            </div>

            <div class="form-row align-content-center">
                <div class="col">
                    <label for="confirmInput">
                        <h3><fmt:message key="password.confirm"/></h3>
                    </label>
                </div>
                <div class="col py-4">
                    <input id="confirmInput" name="confirmPassword" type="password" value="${form.confirmPassword}" placeholder="<fmt:message key="password.confirm"/>" required>
                </div>
            </div>

            <div class="d-flex justify-content-center">
                <button class="btn brown-button btn-lg"><fmt:message key="registration.submit"/></button>
            </div>
        </form>
    </div>
    </c:otherwise>
</c:choose>


</body>
</html>
