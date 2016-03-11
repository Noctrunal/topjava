package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealService;

import java.util.Collection;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class UserMealRestController {
    @Autowired
    @Qualifier(value = "userMealServiceImpl")
    private UserMealService service;

    public UserMeal save(UserMeal userMeal, int userId) {
        return service.save(userMeal, userId);
    }

    public void delete(int id, int userId) {
        service.delete(id, userId);
    }

    public UserMeal get(int id, int userId) {
        return service.get(id, userId);
    }

    public Collection<UserMeal> getAll(int userId) {
        return service.getAll(userId);
    }
}
