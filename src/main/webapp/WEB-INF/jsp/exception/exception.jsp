<%@page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:include page="../fragments/headTag.jsp"/>

<body>
<jsp:include page="../fragments/bodyHeader.jsp"/>

<div class="jumbotron">
    <div class="container">
        <br>
        <h4>Application error: </h4>
        <h2>${exception.message}</h2>
<!--
<c:forEach items="${exception.stackTrace}" var="stackTrace">
    ${stackTrace}
</c:forEach>
-->
    </div>
</div>
<jsp:include page="../fragments/footer.jsp"/>
<script type="text/javascript" src="webjars/jquery/2.2.3/jquery.min.js"></script>
<script type="text/javascript" src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</body>
</html>