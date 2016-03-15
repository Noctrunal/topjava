package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealService;
import ru.javawebinar.topjava.to.UserMealWithExceed;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class UserMealRestController {
    @Autowired
    @Qualifier(value = "userMealServiceImpl")
    private UserMealService service;

    public UserMeal save(UserMeal userMeal) {
        return service.save(userMeal, LoggedUser.id());
    }

    public void delete(int id) throws NotFoundException {
        service.delete(id, LoggedUser.id());
    }

    public UserMeal get(int id) throws NotFoundException {
        return service.get(id, LoggedUser.id());
    }

    public List<UserMealWithExceed> getAll() {
        return UserMealsUtil.getWithExceeded(service.getAll(LoggedUser.id()), UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<UserMealWithExceed> getAllFilteredByDate(LocalDate startTime, LocalDate endTime) {
        return UserMealsUtil.getFilteredWithExceededByDate(service.getAll(LoggedUser.id()), startTime, endTime, UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<UserMealWithExceed> getAllFilteredByTime(LocalTime startTime, LocalTime endTime) {
        return UserMealsUtil.getFilteredWithExceeded(service.getAll(LoggedUser.id()), startTime, endTime, UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<UserMealWithExceed> getAllFilteredByDateTime(LocalDateTime startTime, LocalDateTime endTime) {
        return UserMealsUtil.getFilteredWithExceededByDateTime(service.getAll(LoggedUser.id()), startTime, endTime, UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
    }
}
