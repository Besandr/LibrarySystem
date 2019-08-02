<%@ page contentType="text/html;charset=UTF-8"%>

<c:if test="${empty sessionScope.language}">
    <c:set var="language" value="${applicationScope.language}" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.language}" />
<fmt:setBundle basename="textContent" />

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html>
<head>
    <title>!!!!!</title>
</head>
<body>
<h1>Unfortunately bad things occurred</h1>
<a href="${contextPath}/title" class="link createLink">Continue</a>
</body>
</html>
