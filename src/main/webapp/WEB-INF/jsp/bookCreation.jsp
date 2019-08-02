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
    <c:import url="adminCabinet.jsp"/>

    <title><fmt:message key="bookManagement.title"/></title>
</head>
<body>

<div>
    <c:if test="${errors.hasErrors}">
        <p  style="color:red"><fmt:message key="bookManagement.errors"/></p>
    </c:if>
</div>



<div>
    <form action="${contextPath}/admin/createBook.do" method="post">

        <c:if test="${errors.errorsMap['newAuthors'] != null}">
            <div style="color:red"><fmt:message key="${errors.errorsMap['newAuthors']}"/></div>
        </c:if>

        <div id="authorSelects">
            <div><fmt:message key="bookManagement.existentAuthor"/></div>
            <br>
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

        <table id="newAuthorsTable">
            <tr>
                <th>FIRST NAME</th>
                <th>LAST NAME</th>
            </tr>
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
                <th>WORD</th>
            </tr>
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

        <button><fmt:message key="bookManagement.addBook"/></button>
    </form>
</div>

</body>

<script>
    function addAuthorSelect() {
        var elmnt = document.getElementById("authorSelect");
        var cln = elmnt.cloneNode(true);
        document.getElementById("authorSelects").appendChild(cln);
    }
</script>
<script>
    function addNewAuthorRow() {
        var elmnt = document.getElementById("newAuthor");
        var cln = elmnt.cloneNode(true);
        document.getElementById("newAuthorsTable").appendChild(cln);
    }
</script>
<script>
    function addKeywordSelect() {
        var elmnt = document.getElementById("keywordSelect");
        var cln = elmnt.cloneNode(true);
        document.getElementById("keywordSelects").appendChild(cln);
    }
</script>
<script>
    function addNewKeywordRow() {
        var elmnt = document.getElementById("newKeyword");
        var cln = elmnt.cloneNode(true);
        document.getElementById("newKeywordsTable").appendChild(cln);
    }
</script>
</html>
