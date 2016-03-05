package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();

    public static List<UserMealWithExceed> getUserMealWithExceeds() {
        return userMealWithExceeds;
    }

    private static final Logger LOG = getLogger(MealServlet.class);

    //TODO Работа с GET запросами HTTP
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.debug("on doGet() method");

        request.setAttribute("userMealWithExceeds", getUserMealWithExceeds());
        request.getRequestDispatcher("/mealList.jsp").forward(request, response);

    }

    //TODO Работа с POST запросами HTTP
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOG.debug("on doPost() method");

        switch (request.getParameter("action").toLowerCase()) {
            //TODO Добавление
            case "add":
                userMealWithExceeds.add(new UserMealWithExceed(LocalDateTime.parse(request.getParameter("Date")),
                        request.getParameter("Description"), Integer.parseInt(request.getParameter("Calories"))));
                LOG.debug("Added new object: " + userMealWithExceeds);
                break;
            //TODO Удаление
            case "delete":
                UserMealWithExceed deleteUserMeal = null;
                for (UserMealWithExceed userMeal : getUserMealWithExceeds()) {
                    if (userMeal.getDateTime().toString().equals(request.getParameter("Date"))) {
                        LOG.debug("Object for delete: " + userMeal);
                        deleteUserMeal = userMeal;
                    }
                }
                userMealWithExceeds.remove(deleteUserMeal);
                LOG.debug("After delete: " + userMealWithExceeds);
                break;
            //TODO Редактирование
            case "edit":
                UserMealWithExceed editUserMeal = null;
                int index = 0;
                for (UserMealWithExceed userMeal : getUserMealWithExceeds()) {
                    if (userMeal.getDateTime().toString().equals(request.getParameter("Date"))) {
                        index = userMealWithExceeds.indexOf(userMeal);
                        LOG.debug("Index of object for edit: " + index);
                        editUserMeal = new UserMealWithExceed(LocalDateTime.parse(request.getParameter("Date")), request.getParameter("Description"), Integer.parseInt(request.getParameter("Calories")));
                    }
                }
                userMealWithExceeds.set(index, editUserMeal);
                LOG.debug("After edit: " + userMealWithExceeds);
                break;
            //TODO Поиск
            case "search":
                UserMealWithExceed searchUserMeal = null;
                for (UserMealWithExceed userMeal : getUserMealWithExceeds()) {
                    if (userMeal.getDateTime().toString().equals(request.getParameter("Date"))) {
                        LOG.debug("Searched object: " + userMeal);
                        searchUserMeal = userMeal;
                    }
                }
                request.setAttribute("searchUserMeal", searchUserMeal);
                break;
        }

        LOG.debug("User meal list: " + userMealWithExceeds);

        doGet(request, response);
    }
}
