${meal.dateTime.toLocalDate()}  ${meal.dateTime.toLocalTime()}
<%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title><fmt:message key="app.meals.title"/></title>
    <link rel="stylesheet" href="/topjava/resources/css/style.css">
</head>
<body>
<section>
    <h2><a href="/topjava/index"><fmt:message key="app.meals.home"/> </a></h2>
    <h3><fmt:message key="app.meals.title"/></h3>
    <form method="post" action="/topjava/meals/action/filter">
        <dl>
            <dt><fmt:message key="app.meals.fromdate"/></dt>
            <dd><input type="date" name="startDate" value="${startDate}"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="app.meals.todate"/></dt>
            <dd><input type="date" name="endDate" value="${endDate}"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="app.meals.fromtime"/></dt>
            <dd><input type="time" name="startTime" value="${startTime}"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="app.meals.totime"/></dt>
            <dd><input type="time" name="endTime" value="${endTime}"></dd>
        </dl>
        <button type="submit"><fmt:message key="app.meals.filter"/></button>
    </form>
    <hr>
    <a href="/topjava/meals/action/create"><fmt:message key="app.meals.add"/> </a>
    <hr>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><fmt:message key="app.meals.date"/></th>
            <th><fmt:message key="app.meals.description"/></th>
            <th><fmt:message key="app.meals.calories"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${mealList}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.UserMealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                    <%=TimeUtil.toString(meal.getDateTime())%>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="/topjava/meals/action/update?id=${meal.id}"><fmt:message key="app.meals.update"/> </a></td>
                <td><a href="/topjava/meals/action/delete?id=${meal.id}"><fmt:message key="app.meals.delete"/> </a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
