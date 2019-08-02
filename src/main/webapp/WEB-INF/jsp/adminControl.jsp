<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty sessionScope.language}">
    <c:set var="language" value="${applicationScope.language}" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.language}" />
<fmt:setBundle basename="textContent" />

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<a href="${contextPath}/admin/unapprovedLoans">
    <fmt:message key="adminControl.unapprovedLoans"/>
</a>
<a href="${contextPath}/admin/activeLoans">
    <fmt:message key="adminControl.activeLoans"/>
</a>
<a href="${contextPath}/admin/createBook">
    <fmt:message key="adminControl.createBook"/>
</a>
