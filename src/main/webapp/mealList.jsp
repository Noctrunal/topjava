<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Calories List</title>
</head>
<body>
<!-- Форма для заполнения данных о калориях -->
<form name="postData" method="post" >
    <table align="center" border="1px">
        <tr>
            <td>Date</td>
            <td><input type="text" name="Date" value="${searchUserMeal.dateTime}"/></td>
        </tr>

        <tr>
            <td>Description</td>
            <td><input type="text" name="Description" value="${searchUserMeal.description}"/></td>
        </tr>

        <tr>
            <td>Calories</td>
            <td><input type="text" name="Calories" value="${searchUserMeal.calories}"/></td>
        </tr>
    </table>
    <div align="center">
        <input type="submit" name="action" value="Add" />
        <input type="submit" name="action" value="Delete" />
        <input type="submit" name="action" value="Edit" />
        <input type="submit" name="action" value="Search" />
    </div>
</form>
<!-- Таблица для отображения данных о калориях -->
<form name="getData" method="get" >
    <table align="center" border="1px" >
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>

        <c:forEach var="userMeal" items="${userMealWithExceeds}">
            <tr>
                <td>${userMeal.dateTime}</td>
                <td>${userMeal.description}</td>
                <td>${userMeal.calories}</td>
            </tr>
        </c:forEach>
    </table>
</form>
</body>
</html>
