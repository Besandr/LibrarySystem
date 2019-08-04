<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty sessionScope.language}">
    <c:set var="language" value="${applicationScope.language}" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.language}" />
<fmt:setBundle basename="textContent" />

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<a href="${contextPath}/admin/loansManagement">
    <fmt:message key="adminControl.loansManagement"/>
</a>
<a href="${contextPath}/admin/bookManagement">
    <fmt:message key="adminControl.bookManagement"/>
</a>
<a href="${contextPath}/admin/bookcaseManagement">
    <fmt:message key="bookcaseManagement.title"/>
</a>
