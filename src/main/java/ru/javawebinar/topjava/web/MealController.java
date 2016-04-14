package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealService;
import ru.javawebinar.topjava.to.UserMealWithExceed;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class MealController {
    private static final Logger LOG = getLogger(MealController.class);
    @Autowired
    private UserMealService service;

    @RequestMapping(value = "/index")
    public String root() {
        LOG.info("return Home page");
        return "index";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    public String getAll(Model model) {
        int id = LoggedUser.id();
        int caloriesPerDay = LoggedUser.getCaloriesPerDay();
        model.addAttribute("mealList", UserMealsUtil.getWithExceeded(service.getAll(id), caloriesPerDay));
        LOG.info("getAll for User {}", id);
        return "mealList";
    }

    @RequestMapping(value = "/meals/action/filter", method = RequestMethod.POST)
    public String getAllBetween(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                                @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        LocalDate afterDate = TimeUtil.parseLocalDate(startDate);
        LocalDate beforeDate = TimeUtil.parseLocalDate(endDate);
        LocalTime afterTime = TimeUtil.parseLocalTime(startTime);
        LocalTime beforeTime = TimeUtil.parseLocalTime(endTime);
        int id = LoggedUser.id();
        int caloriesPerDay = LoggedUser.getCaloriesPerDay();
        List<UserMealWithExceed> filteredWithExceeded = UserMealsUtil.getFilteredWithExceeded(service.getBetweenDates(
                afterDate != null ? afterDate : TimeUtil.MIN_DATE, beforeDate != null ? beforeDate : TimeUtil.MAX_DATE, id),
                afterTime != null ? afterTime : LocalTime.MIN, beforeTime != null ? beforeTime : LocalTime.MAX, caloriesPerDay);
        model.addAttribute("mealList", filteredWithExceeded);
        LOG.info("getAllBetween after date {} before date {} after time {} before time {} for User {}", afterDate, beforeDate, afterTime, beforeTime, id);
        return "mealList";
    }

    @RequestMapping(value = "/meals/action/create", method = RequestMethod.GET)
    public String getAddPage(Model model) {
        final UserMeal meal = new UserMeal(LocalDateTime.now(), "", 1000);
        model.addAttribute("meal", meal);
        LOG.info("getAddPage");
        return "mealCreate";
    }

    @RequestMapping(value = "/meals/action/create", method = RequestMethod.POST)
    public String save(Model model, @RequestParam("dateTime") String dateTime, @RequestParam("description") String description, @RequestParam("calories") String calories) {
        final UserMeal meal = new UserMeal(LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
        int userId = LoggedUser.id();
        service.save(meal, userId);
        model.addAttribute("meal", meal);
        LOG.info("save Meal {} for User {}", meal.getId(), userId);
        return "redirect:/meals";
    }

    @RequestMapping(value = "/meals/action/delete", method = RequestMethod.GET)
    public String delete(Model model, @RequestParam("id") String id) {
        int userId = LoggedUser.id();
        service.delete(Integer.parseInt(id), userId);
        model.addAttribute("mealList", service.getAll(userId));
        LOG.info("delete Meal {} for User {}", id, userId);
        return "redirect:/meals";
    }

    @RequestMapping(value = "/meals/action/update", method = RequestMethod.GET)
    public String getEditPage(Model model, @RequestParam("id") String id) {
        int userId = LoggedUser.id();
        final UserMeal meal = service.get(Integer.parseInt(id), userId);
        model.addAttribute("meal", meal);
        LOG.info("getEditPage");
        return "mealEdit";
    }

    @RequestMapping(value = "/meals/action/update", method = RequestMethod.POST)
    public String update(Model model, @RequestParam("id") String id, @RequestParam("dateTime") String dateTime,
                         @RequestParam("description") String description, @RequestParam("calories") String calories) {
        int userId = LoggedUser.id();
        UserMeal meal = service.get(Integer.parseInt(id), userId);
        meal.setDateTime(LocalDateTime.parse(dateTime));
        meal.setDescription(description);
        meal.setCalories(Integer.parseInt(calories));
        service.update(meal, userId);
        model.addAttribute("meal", meal);
        LOG.info("update Meal {} for User {}", meal.getId(), userId);
        return "redirect:/meals";
    }
}
