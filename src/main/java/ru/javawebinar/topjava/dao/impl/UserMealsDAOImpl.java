package ru.javawebinar.topjava.dao.impl;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.UserMealsDAO;
import ru.javawebinar.topjava.model.UserMeal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class UserMealsDAOImpl implements UserMealsDAO {
    private static final Logger LOG = getLogger(UserMealsDAOImpl.class);

    private Map<Integer, UserMeal> mealMap = new ConcurrentHashMap<>();

    private AtomicInteger generateId = new AtomicInteger();

    @Override
    public void saveUserMeal(UserMeal userMeal) {
        userMeal.setId(generateId.incrementAndGet());
        mealMap.put(userMeal.getId(), userMeal);
        LOG.debug("Saving User Meal in DAO impl class: " + userMeal);
    }

    @Override
    public void removeUserMeal(int id) {
        mealMap.remove(id);
        LOG.debug("Removing User Meal in DAO impl class with ID: " + id);
    }

    @Override
    public void editUserMeal(int id, UserMeal userMeal) {
        mealMap.put(id, userMeal);
        LOG.debug("Editing User Meal in DAO impl class with ID: " + id + " and parameters: " + userMeal);
    }

    @Override
    public UserMeal getUserMeal(int id) {
        LOG.debug("Getting User Meal in DAO impl class with ID: " + id);
        return mealMap.get(id);
    }

    @Override
    public List<UserMeal> getAllUserMeals() {
        LOG.info("Returning All User Meals in DAO impl class");
        return new ArrayList<>(mealMap.values());
    }
}
