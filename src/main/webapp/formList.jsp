<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add new User Meal</title>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8" />
</head>
<body>
<!-- Форма для заполнения данных о калориях -->
<form name="postData" method="post" >
    <table align="center" border="1px">
        <tr>
            <td>Date</td>
            <td><input type="text" name="date" value="<c:out value="${userMeal.dateTimeFormat}" />"/></td>
        </tr>

        <tr>
            <td>Description</td>
            <td><input type="text" name="description" value="<c:out value="${userMeal.description}" />"/></td>
        </tr>

        <tr>
            <td>Calories</td>
            <td><input type="text" name="calories" value="<c:out value="${userMeal.calories}" />"/></td>
        </tr>
    </table>
    <div align="center">
        <input type="submit" name="action" value="Submit" />
    </div>
</form>
</body>
</html>
