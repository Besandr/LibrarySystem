<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty sessionScope.language}">
    <c:set var="language" value="${applicationScope.language}" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="textContent"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="bookManagement.creationTitle"/></title>
    <script src="${contextPath}/resources/js/form.js"></script>
</head>
<body>

<div><c:import url="adminControl.jsp"/></div>

<div>
    <c:if test="${errors.hasErrors}">
        <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
            <h3><fmt:message key="bookManagement.bookForm.errors"/></h3>
        </div>
    </c:if>
</div>

<div class="d-flex justify-content-center">
    <form action="${contextPath}/admin/bookManagement/createBook.do" method="post">
        <div class="form-row justify-content-center align-items-center">
            <div id="authorSelects">
                <div class="d-flex justify-content-center">
                    <h4><fmt:message key="bookManagement.existentAuthor"/></h4>
                </div>
                <%--    Checking for existing errors in previous form sumbiting--%>
                <c:if test="${errors.errorsMap['newAuthors'] != null}">
                    <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                        <fmt:message key="${errors.errorsMap['newAuthors']}"/>
                    </div>
                </c:if>
                <br>
                <%--        Authors selects from saved in a request data--%>
                <c:if test="${not empty form.oldAuthorsId}">
                    <c:forEach items="${form.oldAuthorsId}" var="authorId">
                        <div>
                            <select class="custom-select mx-sm-1" name="oldAuthorId">
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
                        </div>
                        <br>
                    </c:forEach>
                </c:if>
                <%--        Select for "old authors" which can be added to book--%>
                <div id="authorSelect">
                    <select class="custom-select mx-sm-1" name="oldAuthorId">
                        <option disabled selected value><fmt:message key="bookManagement.chooseAuthor"/></option>
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.id}">${author.firstName} ${author.lastName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <button class="btn btn-sm btn-group brown-button align-self-end mx-sm-4 mb-1" type="button" onclick="addAuthorSelect()">+</button>
        </div>
        <br>

        <div class="form-row align-items-center">
            <div>
                <div class="d-flex justify-content-center">
                    <h4><fmt:message key="bookManagement.newAuthor"/></h4>
                </div>
                <%--Table for creating a new authors--%>
                <table id="newAuthorsTable">
                    <tr>
                        <th><fmt:message key="firstName"/></th>
                        <th><fmt:message key="lastName"/></th>
                    </tr>
                    <%--        New authors from saved in a request data--%>
                    <c:if test="${not empty form.newAuthorLastNames}">
                        <c:forEach items="${form.newAuthorLastNames}" varStatus="loop">
                            <tr>
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
                </div>
            <button class="btn btn-sm btn-group brown-button align-self-end mx-sm-4" type="button" onclick="addNewAuthorRow()">+</button>
        </div>
        <br>

        <div class="form-row justify-content-center align-items-center">
            <div id="keywordSelects">
                <div class="d-flex justify-content-center">
                    <h4><fmt:message key="bookManagement.existentKeyword"/></h4>
                </div>
                <br>
                <%--            Keywords selects from saved in a request data--%>
                <c:if test="${not empty form.oldKeywordsId}">
                    <c:forEach items="${form.oldKeywordsId}" var="keywordId">
                        <div>
                            <select class="custom-select" name="oldKeywordId">
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
                        </div>
                        <br>
                    </c:forEach>
                </c:if>
                <%--Select for "old keywords" which can be added to book--%>
                <div id="keywordSelect">
                    <select class="custom-select" name="oldKeywordsId">
                        <option disabled selected value><fmt:message key="bookManagement.chooseKeyword"/></option>
                        <c:forEach var="keyword" items="${keywords}">
                            <option value="${keyword.id}">${keyword.word}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <button class="btn btn-sm btn-group brown-button align-self-end mx-sm-4 mb-1" type="button" onclick="addKeywordSelect()">+</button>
        </div>
            <br>

        <div class="form-row justify-content-center align-items-center">
            <div>
                <div class="d-flex justify-content-center">
                    <h4><fmt:message key="bookManagement.newKeyword"/></h4>
                </div>

                <table id="newKeywordsTable">
                <tr>
                    <th><fmt:message key="keyword"/></th>
                </tr>
                <%--        New keywords from stored in request data--%>
                <c:if test="${not empty form.newKeywords}">
                    <c:forEach items="${form.newKeywords}" var="keyword">
                        <tr>
                            <td><input type="text" name="newKeyword" value="${keyword}"></td>
                        </tr>
                    </c:forEach>
                </c:if>
                <%--        Fields for new keywords--%>
                <tr id="newKeyword">
                    <td><input type="text" name="newKeyword"></td>
                </tr>
            </table>
            </div>
            <button class="btn btn-sm btn-group brown-button align-self-end " type="button" onclick="addNewKeywordRow()">+</button>
        </div>
        <br>

        <div class="form-row justify-content-center">
            <label for="bookTitle">
                <h4><fmt:message key="bookManagement.bookTitle"/></h4>
            </label>
            <c:choose>
                <c:when test="${errors.errorsMap['title'] != null}">
                    <input id="bookTitle" class="form-control is-invalid" type="text" name="title" value="${form.title}" required>
                    <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                        <fmt:message key="${errors.errorsMap['title']}"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <input id="bookTitle" class="form-control" type="text" name="title" value="${form.title}" required>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="form-row justify-content-center">
            <label for="bookIssueYear">
                <h4><fmt:message key="bookManagement.bookYear"/></h4>
            </label>
            <c:choose>
                <c:when test="${errors.errorsMap['year'] != null}">
                    <input id="bookIssueYear" class="form-control is-invalid" type="text" name="year" value="${form.yearString}" required>
                    <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                        <fmt:message key="${errors.errorsMap['year']}"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <input id="bookIssueYear" class="form-control" type="text" name="year" value="${form.yearString}" required>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="form-row justify-content-center">
            <label for="bookDescription">
                <h4><fmt:message key="bookManagement.bookDescription"/></h4>
            </label>
            <c:choose>
                <c:when test="${errors.errorsMap['description'] != null}">
                    <input id="bookDescription" class="form-control is-invalid" type="text" name="description" value="${form.description}" required>
                    <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                        <fmt:message key="${errors.errorsMap['description']}"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <input id="bookDescription" class="form-control" type="text" name="description" value="${form.description}" required>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="d-flex justify-content-center">
            <button class="btn btn-lg brown-button mt-3"><fmt:message key="bookManagement.createBook"/></button>
        </div>
    </form>
</div>

</body>


</html>