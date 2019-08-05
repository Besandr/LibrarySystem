<%@ page contentType="text/html;charset=UTF-8"%>
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
    <title><fmt:message key="bookManagement.updatingTitle"/></title>
    <script src="${contextPath}/resources/js/form.js"></script>
</head>
<body>

<div><c:import url="adminCabinet.jsp"/></div>

<div>
    <c:if test="${errors.hasErrors}">
        <p style="color:red"><fmt:message key="bookManagement.bookForm.errors"/></p>
    </c:if>
</div>

<div>
    <form action="${contextPath}/admin/bookManagement/updateBook.do" method="post">
        <%--    Checking for existing errors in previous form sumbiting--%>
        <c:if test="${errors.errorsMap['newAuthors'] != null}">
            <div style="color:red"><fmt:message key="${errors.errorsMap['newAuthors']}"/></div>
        </c:if>

        <div id="authorSelects">
            <div><fmt:message key="bookManagement.existentAuthor"/></div>
            <br>
            <%--        Authors selects from saved in a request data--%>
            <c:if test="${not empty form.oldAuthorsId}">
                <c:forEach items="${form.oldAuthorsId}" var="authorId">
                    <div id="oldAuthor${authorId}">
                        <select name="oldAuthorId">
                            <c:forEach var="author" items="${authors}">
                                <c:choose>
                                    <c:when test="${author.id == authorId}">
                                        <option selected value="${author.id}">${author.firstName} ${author.lastName}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${author.id}">${author.firstName} ${author.lastName}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                        <button type="button>" onclick="deleteNodeById('oldAuthor${authorId}')">-</button>
                    </div>
                    <br>
                </c:forEach>
            </c:if>
            <%--        Select for "old authors" which can be added to book--%>
            <div id="authorSelect">
                <select name="oldAuthorId">
                    <option disabled selected value><fmt:message key="bookManagement.chooseAuthor"/></option>
                    <c:forEach var="author" items="${authors}">
                        <option value="${author.id}">${author.firstName} ${author.lastName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <button type="button" onclick="addAuthorSelect()">+</button>
        <br>

        <div><fmt:message key="bookManagement.newAuthor"/></div>
        <%--        Table for creating a new authors--%>
        <table id="newAuthorsTable">
            <tr>
                <th><fmt:message key="firstName"/></th>
                <th><fmt:message key="lastName"/></th>
            </tr>
            <%--        New authors from saved in a request data--%>
            <c:if test="${not empty form.newAuthorLastNames}">
                <c:forEach items="${form.newAuthorLastNames}" varStatus="loop">
                    <tr id="newAuthor${loop.index}">
                        <td><input type="text" name="newAuthorFirstName" value="${form.newAuthorFirstNames[loop.index]}"></td>
                        <td><input type="text" name="newAuthorLastName" value="${form.newAuthorLastNames[loop.index]}"></td>
                    </tr>
                </c:forEach>
            </c:if>
            <%--        Fields for new author--%>
            <tr id="newAuthor">
                <td><input type="text" name="newAuthorFirstName"></td>
                <td><input type="text" name="newAuthorLastName"></td>
            </tr>
        </table>
        <button type="button" onclick="addNewAuthorRow()">+</button>
        <br>

        <div id="keywordSelects">
            <div><fmt:message key="bookManagement.existentKeyword"/></div>
            <br>
            <%--            Keywords selects from saved in a request data--%>
            <c:if test="${not empty form.oldKeywordsId}">
                <c:forEach items="${form.oldKeywordsId}" var="keywordId">
                    <div id="oldKeyword${keywordId}">
                        <select name="oldKeywordsId">
                            <c:forEach var="keyword" items="${keywords}">
                                <c:choose>
                                    <c:when test="${keyword.id == keywordId}">
                                        <option selected value="${keyword.id}">${keyword.word}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${keyword.id}">${keyword.word}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                        <button type="button>" onclick="deleteNodeById('oldKeyword${keywordId}')">-</button>
                    </div>
                    <br>
                </c:forEach>
            </c:if>
            <%--Select for "old keywords" which can be added to book--%>
            <div id="keywordSelect">
                <select name="oldKeywordsId">
                    <option disabled selected value><fmt:message key="bookManagement.chooseKeyword"/></option>
                    <c:forEach var="keyword" items="${keywords}">
                        <option value="${keyword.id}">${keyword.word}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <button type="button" onclick="addKeywordSelect()">+</button>
        <br>
        <div><fmt:message key="bookManagement.newKeyword"/></div>
        <br>

        <table id="newKeywordsTable">
            <tr>
                <th><fmt:message key="keyword"/></th>
            </tr>
            <%--        New keywords from stored in request data--%>
            <c:if test="${not empty form.newKeywords}">
                <c:forEach items="${form.newKeywords}" var="keyword">
                    <tr id="newWord${keyword}">
                        <td><input type="text" name="newKeyword" value="${keyword}"></td>
                    </tr>
                </c:forEach>
            </c:if>
            <%--        Fields for new keywords--%>
            <tr id="newKeyword">
                <td><input type="text" name="newKeyword"></td>
            </tr>
        </table>

        <button type="button" onclick="addNewKeywordRow()">+</button>
        <br>

        <div>
            <div><fmt:message key="bookManagement.bookTitle"/></div>
            <c:if test="${errors.errorsMap['title'] != null}">
                <div style="color:red"><fmt:message key="${errors.errorsMap['title']}"/></div>
            </c:if>
            <div><input type="text" name="title" value="${form.title}" required></div>
        </div>
        <div>
            <div><fmt:message key="bookManagement.bookYear"/></div>
            <c:if test="${errors.errorsMap['year'] != null}">
                <div style="color:red"><fmt:message key="${errors.errorsMap['year']}"/></div>
            </c:if>
            <div><input type="text" name="year" value="${form.yearString}" required></div>
        </div>
        <div>
            <div><fmt:message key="bookManagement.bookDescription"/></div>
            <c:if test="${errors.errorsMap['description'] != null}">
                <div style="color:red"><fmt:message key="${errors.errorsMap['description']}"/></div>
            </c:if>
            <div><input type="text" name="description" value="${form.description}" required></div>
        </div>

        <button><fmt:message key="bookManagement.changeBookProperties"/></button>
    </form>
</div>

</body>
</html>
