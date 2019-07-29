<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.language}">
    <c:set var="language" value="${applicationScope.language}" scope="session"/>
</c:if>

<fmt:setLocale value="${sessionScope.language}" />
<fmt:setBundle basename="textContent" />

<div>
    <a href="title"><fmt:message key="header.home" /></a>
</div>
<br>
<div>
    <form action="changeLanguage">
        <select name="chosenLanguage" onchange="this.form.submit()">
            <option disabled selected value><fmt:message key="header.chooseLanguage"/></option>
            <c:forEach var="languageOption" items="${applicationScope.languages}">
                <c:if test="${sessionScope.language ne languageOption.code}">
                    <option value="${languageOption}">${languageOption.name}</option>
                </c:if>
            </c:forEach>
        </select>
    </form>
</div>

<div>
    <c:choose>
        <c:when test="${sessionScope.loginedUser != null}">
            <div>
                    ${sessionScope.loginedUser.firstName} ${sessionScope.loginedUser.lastName}
            </div>
            <div>
                <a href="logout" onclick="return confirm('<fmt:message key="logout.confirm"/>')">
                    <fmt:message key="logout.button"/>
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div>
                <a href="login">
                    <fmt:message key="header.login"/>
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<hr>