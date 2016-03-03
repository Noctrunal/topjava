package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.*;

public class MealServlet extends HttpServlet {

    private static List<UserMealWithExceed> userMealWithExceeds = null;

    private static final Logger LOG = getLogger(MealServlet.class);

    static {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        userMealWithExceeds = UserMealsUtil.getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    public static List<UserMealWithExceed> getUserMealWithExceeds() {
        return userMealWithExceeds;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("on doGet method");

        response.setContentType("text/html; charset=utf-8");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<body>");
        out.println("<table border=1 align=center>");

        for (UserMealWithExceed userMeal : getUserMealWithExceeds()) {

            out.println("<tr>");

            if (userMeal.isExceed()) {

                out.println("<td bgcolor=red>" + userMeal.getDateTime() + "</td>");
                out.println("<td bgcolor=red>" + userMeal.getDescription() + "</td>");
                out.println("<td bgcolor=red>" + userMeal.getCalories() + "</td>");

            } else {

                out.println("<td>" + userMeal.getDateTime() + "</td>");
                out.println("<td>" + userMeal.getDescription() + "</td>");
                out.println("<td>" + userMeal.getCalories() + "</td>");

            }

            out.println("</tr>");

        }

        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO implement tomorrow
    }
}
