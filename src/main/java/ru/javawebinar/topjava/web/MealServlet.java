package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.web.meal.UserMealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private UserMealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            controller = context.getBean(UserMealRestController.class);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        UserMeal userMeal = new UserMeal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        LOG.info(userMeal.isNew() ? "Create {}" : "Update {}", userMeal);
        controller.save(userMeal);
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            LOG.info("getAll");
            request.setAttribute("mealList",
                    controller.getAll());
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int id = getId(request);
            LOG.info("Delete {}", id);
            controller.delete(id);
            response.sendRedirect("meals");
        } else if (action.equals("filter")) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm");
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            String fromTime = request.getParameter("fromTime");
            String toTime = request.getParameter("toTime");
            request.setAttribute("mealList", !fromDate.equals("") && !toDate.equals("") && fromTime.equals("") && toTime.equals("") ?
            controller.getAllFilteredByDate(LocalDate.parse(fromDate, dateFormat), LocalDate.parse(toDate, dateFormat)):
            !fromTime.equals("") && !toTime.equals("") && fromDate.equals("") && toDate.equals("") ? controller.getAllFilteredByTime(LocalTime.parse(fromTime, timeFormat), LocalTime.parse(toTime, timeFormat)):
                    controller.getAllFilteredByDateTime(LocalDateTime.parse(fromDate + " " + fromTime, dateTimeFormat), LocalDateTime.parse(toDate + " " + toTime, dateTimeFormat)));
            LOG.info(!fromDate.equals("") && !toDate.equals("") && fromTime.equals("") && toTime.equals("") ?
            "getAllFilteredByDate" : !fromTime.equals("") && !toTime.equals("") && fromDate.equals("") && toDate.equals("") ?
            "getAllFilteredByTime" : "getAllFilteredByDateTime");
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        } else {
            final UserMeal meal = action.equals("create") ?
                    new UserMeal(LocalDateTime.now(), "", 1000) :
                    controller.get(getId(request));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
