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
            <select name="authorId">
                <option disabled selected value><fmt:message key="bookSearch.chooseAuthor"/></option>
                <c:forEach var="author" items="${authors}">
                    <option value="${author.id}">${author.lastName} ${author.firstName}</option>
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
            <select name="keywordId">
                <option disabled selected value><fmt:message key="bookSearch.chooseKeyword"/></option>
                <c:forEach var="keyword" items="${keywords}">
                    <option value="${keyword.id}">${keyword.word}</option>
                </c:forEach>
            </select>
        </label>
        <br>
        <button><fmt:message key="bookSearch.findBook"/> </button>
    </form>
</div>
<c:if test="${not empty books}">
    <hr>
    <div>
        <fmt:message key="bookSearch.searchResult"/>:
    </div>
    <div>
        <table>

            <tr>
                <th><fmt:message key="bookSearch.result.choice"/></th>
                <th><fmt:message key="bookSearch.result.author"/></th>
                <th><fmt:message key="bookSearch.result.title"/></th>
                <th><fmt:message key="bookSearch.result.year"/></th>
                <th><fmt:message key="bookSearch.result.description"/></th>
            </tr>

            <c:forEach var="bookDTO" items="${books}">
                <tr>
                    <td>
                        <form action="orderBook.do" method="post">
                            <input type="text" name="bookId" value="${bookDTO.book.id}" hidden>
                            <button><fmt:message key="bookSearch.result.order"/></button>
                        </form>
                    </td>

                    <td>
                        <div>
                        <ul style="list-style-type:none;">
                            <li>
                                <c:forEach var="author" items="${bookDTO.authors}">
                                ${author.firstName} ${author.lastName}
                                </c:forEach>
                            </li>
                        </ul>
                        </div></td>
                    <td>
                        ${bookDTO.book.title}
                    </td>
                    <td>
                        ${bookDTO.book.year}
                    </td>
                    <td>
                        ${bookDTO.book.description}
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</c:if>

<c:if test="${not empty orderResult}">
    <div>
        <fmt:message key="${orderResult}"/>
    </div>
</c:if>
</body>
</html>
