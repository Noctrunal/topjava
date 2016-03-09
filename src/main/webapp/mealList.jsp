<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Подсчет калорий</title>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8" />
</head>
<body>
<!-- Таблица для отображения данных о калориях -->
<h1 align="center">Список еды</h1>
<form name="getData" method="get" >
    <table align="center" border="1px" >
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th>Actions</th>
        </tr>

        <c:forEach var="userMeal" items="${userMealWithExceeds}">
            <tr>
                <c:if test="${userMeal.exceed}" >
                <td style="color: red"><c:out value="${userMeal.dateTimeFormat}" /></td>
                <td style="color: red"><c:out value="${userMeal.description}"/></td>
                <td style="color: red"><c:out value="${userMeal.calories}"/></td>
                </c:if>
                <c:if test="${!userMeal.exceed}" >
                <td style="color: green"><c:out value="${userMeal.dateTimeFormat}" /></td>
                <td style="color: green"><c:out value="${userMeal.description}" /></td>
                <td style="color: green"><c:out value="${userMeal.calories}" /></td>
                </c:if>
                <td><a href="meals?action=edit&id=<c:out value="${userMeal.id}" />" >Edit</a>
                    <a href="meals?action=delete&id=<c:out value="${userMeal.id}" />" >Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <div align="center" >
    <p><a href="meals?action=add">Добавить еду</a> </p>
    </div>
</form>
</body>
</html>
