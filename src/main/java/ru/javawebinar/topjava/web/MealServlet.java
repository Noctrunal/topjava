package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.UserMealsDAO;
import ru.javawebinar.topjava.dao.impl.UserMealsDAOImpl;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);

    public static final String LIST_OF_MEALS = "/mealList.jsp";

    public static final String INSERT_OR_EDIT = "/formList.jsp";

    private UserMealsDAO userMealsDAO;

    public MealServlet() {
        userMealsDAO = new UserMealsDAOImpl();
        LOG.info("Initializing Meal Servlet with DAO class");
    }

    //TODO Работа с GET запросами HTTP
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.info("in doGet() method body");

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        if (request.getParameter("action") != null) {
            String action = request.getParameter("action").toLowerCase();
            String forward = "";
            switch (action) {
                case "edit": {
                    forward = INSERT_OR_EDIT;
                    int id = Integer.parseInt(request.getParameter("id"));
                    UserMeal userMeal = userMealsDAO.getUserMeal(id);
                    request.setAttribute("userMeal", userMeal);
                    LOG.debug("User Meal for edit: " + userMeal);
                    break;
                }
                case "add":
                    forward = INSERT_OR_EDIT;
                    break;
                case "delete": {
                    forward = LIST_OF_MEALS;
                    int id = Integer.parseInt(request.getParameter("id"));
                    UserMeal deleteUserMeal = userMealsDAO.getUserMeal(id);
                    userMealsDAO.removeUserMeal(deleteUserMeal.getId());
                    request.setAttribute("userMealWithExceeds",
                            UserMealsUtil.getUserMealsWithExceeds(userMealsDAO.getAllUserMeals(), UserMealsUtil.CALORIES_PER_DAY));
                    LOG.debug("User Meal for remove: " + deleteUserMeal);
                    LOG.info("After removing user meal: " + userMealsDAO.getAllUserMeals());
                    break;
                }
                default:
                    forward = LIST_OF_MEALS;
                    request.setAttribute("userMealWithExceeds",
                            UserMealsUtil.getUserMealsWithExceeds(userMealsDAO.getAllUserMeals(), UserMealsUtil.CALORIES_PER_DAY));
                    break;
            }
            RequestDispatcher view = request.getRequestDispatcher(forward);
            view.forward(request, response);
        } else {
            RequestDispatcher view = request.getRequestDispatcher(LIST_OF_MEALS);
            view.forward(request, response);
        }
    }

    //TODO Работа с POST запросами HTTP
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.info("in doPost() method body");

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy' 'HH:mm:ss");
        UserMeal newUserMeal = new UserMeal(LocalDateTime.parse(request.getParameter("date"), formatter),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            userMealsDAO.saveUserMeal(newUserMeal);
            LOG.debug("Saving User Meal: " + newUserMeal);
        } else {
            newUserMeal.setId(Integer.parseInt(id));
            userMealsDAO.editUserMeal(newUserMeal.getId(), newUserMeal);
            LOG.debug("Editing User Meal: " + newUserMeal);
        }

        RequestDispatcher view = request.getRequestDispatcher(LIST_OF_MEALS);
        request.setAttribute("userMealWithExceeds",
                UserMealsUtil.getUserMealsWithExceeds(userMealsDAO.getAllUserMeals(), UserMealsUtil.CALORIES_PER_DAY));
        view.forward(request, response);

        LOG.info("After saving or editing: " + userMealsDAO.getAllUserMeals());
    }
}
