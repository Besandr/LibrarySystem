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

    <title><fmt:message key="bookSearch.title"/></title>
</head>
<body>
<div>
    <h2><fmt:message key="bookSearch.title"/></h2>
</div>
<br>
<div>
    <form action="bookSearch.do">

        <label>
            <fmt:message key="bookSearch.author"/>
            <select name="author">
                <option disabled selected value><fmt:message key="bookSearch.chooseAuthor"/></option>
                <c:forEach var="author" items="${authors}">
                    <option value="${author}">${author.lastName} ${author.firstName}</option>
                </c:forEach>
            </select>
        </label>
        <br>
        <label>
            <fmt:message key="bookSearch.searchBookTitle"/>:
            <input name="bookTitle" type="text">
        </label>
        <br>
        <label>
            <fmt:message key="bookSearch.keyword"/>:
            <select name="keyword">
                <option disabled selected value><fmt:message key="bookSearch.chooseKeyword"/></option>
                <c:forEach var="keyword" items="${keywords}">
                    <option value="${keyword}">${keyword.word}</option>
                </c:forEach>
            </select>
        </label>
        <br>
        <button><fmt:message key="bookSearch.findBook"/> </button>
    </form>
</div>
</body>
</html>
