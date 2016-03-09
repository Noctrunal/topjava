package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.UserMeal;

import java.util.Collection;
import java.util.List;

public interface UserMealsDAO {
    void saveUserMeal(UserMeal userMeal);

    void removeUserMeal(int id);

    void editUserMeal(int id, UserMeal userMeal);

    UserMeal getUserMeal(int id);

    List<UserMeal> getAllUserMeals();
}
