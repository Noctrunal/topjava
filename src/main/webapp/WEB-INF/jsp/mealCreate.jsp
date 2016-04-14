<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title><fmt:message key="app.meals.create"/></title>
    <link rel="stylesheet" href="/topjava/resources/css/style.css">
</head>
<body>
<section>
    <h2><a href="/topjava/index"><fmt:message key="app.meals.home"/> </a></h2>
    <h3><fmt:message key="app.meals.create"/></h3>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.UserMeal" scope="request"/>
    <form method="post" action="create">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><fmt:message key="app.meals.form.datetime"/></dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="app.meals.form.description"/></dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description"></dd>
        </dl>
        <dl>
            <dt><fmt:message key="app.meals.form.calories"/></dt>
            <dd><input type="number" value="${meal.calories}" name="calories"></dd>
        </dl>
        <button type="submit"><fmt:message key="app.meals.save"/></button>
        <button onclick="window.history.back()"><fmt:message key="app.meals.cancel"/></button>
    </form>
</section>
</body>
</html>
