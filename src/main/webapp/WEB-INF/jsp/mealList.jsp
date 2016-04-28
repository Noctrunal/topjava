${meal.dateTime.toLocalDate()}  ${meal.dateTime.toLocalTime()}
<%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<link rel="stylesheet" href="webjars/datatables/1.10.11/css/jquery.dataTables.min.css">
<link rel="stylesheet" href="webjars/datetimepicker/2.5.1/jquery.datetimepicker.css">
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron">
    <div class="container">
        <div class="shadow">
            <section>
                <%--http://stackoverflow.com/questions/10327390/how-should-i-get-root-folder-path-in-jsp-page--%>
                <h3><a href="${pageContext.request.contextPath}">Home</a></h3>
                <h3><fmt:message key="meals.title"/></h3>
                <div class="view-box">
                    <form class="form-horizontal" method="post" id="filter" role="form">
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="startDate">From Date:</label>

                            <div class="col-sm-2">
                                <input name="startDate" id="startDate" type="date">
                            </div>

                            <label class="control-label col-sm-2" for="endDate">To Date:</label>

                            <div class="col-sm-2">
                                <input name="endDate" id="endDate" type="date">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="startTime">From Time:</label>

                            <div class="col-sm-2">
                                <input name="startTime" id="startTime" type="time">
                            </div>

                            <label class="control-label col-sm-2" for="endTime">To Time:</label>

                            <div class="col-sm-2">
                                <input name="endTime" id="endTime" type="time">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-8">
                                <button type="submit" class="btn btn-primary pull-right">Filter</button>
                            </div>
                        </div>
                    </form>
                </div>
                <hr>
                <%--<a href="meals/create">Add Meal</a>--%>
                <hr>
                <div class="view-box">
                    <a class="btn btn-sm btn-info" id="add">Add Meal</a>
                    <table class="table table-striped display" id="datatable">
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Description</th>
                            <th>Calories</th>
                            <th></th>
                            <th></th>
                        </tr>
                        </thead>
                        <c:forEach items="${mealList}" var="meal">
                            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.UserMealWithExceed"/>
                            <tr class="${meal.exceed ? 'exceeded' : 'normal'}" id="${meal.id}">
                                <td>
                                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                                    <%=TimeUtil.toString(meal.getDateTime())%>
                                </td>
                                <td>${meal.description}</td>
                                <td>${meal.calories}</td>
                                <td><a class="btn btn-xs btn-primary edit">Edit</a></td>
                                <td><a class="btn btn-xs btn-danger delete">Delete</a></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </section>
        </div>
    </div>
</div>
</div>
<jsp:include page="fragments/footer.jsp"/>
<div class="modal fade" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"><fmt:message key="meals.edit"/></h2>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" method="post" id="detailsForm">
                    <input type="text" hidden="hidden" id="id" name="id">

                    <div class="form-group">
                        <label for="dateTime" class="control-label col-xs-3">Date time</label>

                        <div class="col-xs-9">
                            <input type="datetime-local" class="form-control" id="dateTime" name="dateTime"
                                   placeholder="Date">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description" class="control-label col-xs-3">Description</label>

                        <div class="col-xs-9">
                            <input type="text" class="form-control" id="description" name="description"
                                   placeholder="Description">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="calories" class="control-label col-xs-3">Calories</label>

                        <div class="col-xs-9">
                            <input type="text" class="form-control" id="calories" name="calories"
                                   placeholder="Calories">
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-offset-3 col-xs-9">
                            <button type="submit" class="btn btn-primary">Save</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="webjars/jquery/2.2.3/jquery.min.js"></script>
<script type="text/javascript" src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script type="text/javascript" src="webjars/datetimepicker/2.5.1/jquery.datetimepicker.js"></script>
<script type="text/javascript" src="webjars/datatables/1.10.11/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="webjars/noty/2.3.8/js/noty/packaged/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="resources/js/datatablesUtil.js"></script>
<script type="text/javascript">
    var ajaxUrl = 'ajax/profile/meals/';
    var datatableApi;

    $(function () {
        datatableApi = $('#datatable').dataTable({
            "bPaginate": false,
            "bInfo": false,
            "aoColumns": [
                {
                    "mData": "dateTime"
                },
                {
                    "mData": "description"
                },
                {
                    "mData": "calories"
                },
                {
                    "sDefaultContent": "",
                    "bSortable": false
                },
                {
                    "sDefaultContent": "",
                    "bSortable": false
                }
            ],
            "aaSorting": [
                [
                    0,
                    "desc"
                ]
            ]
        });
        makeEditable();
    });
</script>
</html>