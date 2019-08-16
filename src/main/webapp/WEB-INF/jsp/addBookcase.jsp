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
    <title><fmt:message key="bookcaseManagement.addingBookcase.title"/></title>
</head>
<body>

<div><c:import url="adminControl.jsp"/></div>

<div class="d-flex justify-content-center my-md-5">
    <h3><fmt:message key="bookcaseManagement.addingBookcase.header"/></h3>
</div>
<br>
<c:if test="${errors.hasErrors}">
    <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
        <h3><fmt:message key="bookcaseManagement.addingBookcase.errors"/></h3>
    </div>
</c:if>

<div class="d-flex justify-content-center">
    <form action="${contextPath}/admin/bookcaseManagement/add.do">

        <div class="form-row">
            <label for="shelves">
                <h3><fmt:message key="bookcaseManagement.addingBookcase.shelves"/></h3>
            </label>
            <c:choose>
                <c:when test="${errors.errorsMap['shelfQuantity'] != null}">
                    <input class="form-control is-invalid" id="shelves" type="text" name="shelfQuantity"
                           value="${form.shelfQuantity}" required>
                    <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                        <fmt:message key="${errors.errorsMap['shelfQuantity']}"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <input class="form-control" id="shelves" type="text" name="shelfQuantity"
                           value="${form.shelfQuantity}" required>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="form-row">
            <label for="cells">
                <h3><fmt:message key="bookcaseManagement.addingBookcase.cells"/></h3>
            </label>
            <c:choose>
                <c:when test="${errors.errorsMap['cellQuantity'] != null}">
                    <input class="form-control is-invalid" id="cells" type="text" name="cellQuantity"
                           value="${form.cellQuantity}" required>
                    <div class="d-flex justify-content-center invalid-feedback font-weight-bold">
                        <fmt:message key="${errors.errorsMap['cellQuantity']}"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <input class="form-control" id="cells" type="text" name="cellQuantity" value="${form.cellQuantity}"
                           required>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="d-flex justify-content-center">
            <button class="btn brown-button btn-lg" style="margin:3%"><fmt:message
                    key="bookcaseManagement.addingBookcase.submit"/></button>
        </div>
    </form>
</div>
</body>
</html>