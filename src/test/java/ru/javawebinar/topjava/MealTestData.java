package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDateTime;

/**
 * GKislin
 * 13.03.2015.
 */
public class MealTestData {

    public static final ModelMatcher<UserMeal, String> MATCHER = new ModelMatcher<>(UserMeal::toString);

    public static final UserMeal USER_MEAL_1 = new UserMeal(1, LocalDateTime.of(2015, 5, 30, 10, 0, 0), "Breakfast", 500);

    public static final UserMeal USER_MEAL_2 = new UserMeal(2, LocalDateTime.of(2015, 5, 30, 13, 0, 0), "Lunch", 1000);

    public static final UserMeal USER_MEAL_3 = new UserMeal(3, LocalDateTime.of(2015, 5, 30, 20, 0, 0), "Dinner", 500);

    public static final UserMeal USER_MEAL_4 = new UserMeal(4, LocalDateTime.of(2015, 5, 31, 10, 0, 0), "Breakfast", 500);

    public static final UserMeal USER_MEAL_5 = new UserMeal(5, LocalDateTime.of(2015, 5, 31, 13, 0, 0), "Lunch", 1000);

    public static final UserMeal USER_MEAL_6 = new UserMeal(6, LocalDateTime.of(2015, 5, 31, 20, 0, 0), "Dinner", 510);

    public static final UserMeal USER_MEAL_1_UPDATE = new UserMeal(1, LocalDateTime.of(2015, 5, 30, 10, 0, 0), "Update Meal", 450);

    public static final UserMeal ADMIN_MEAL_1 = new UserMeal(7, LocalDateTime.of(2015, 5, 30, 10, 0, 0), "Admin Breakfast", 500);

    public static final UserMeal ADMIN_MEAL_2 = new UserMeal(8, LocalDateTime.of(2015, 5, 30, 13, 0, 0), "Admin Lunch", 1000);

    public static final UserMeal ADMIN_MEAL_3 = new UserMeal(9, LocalDateTime.of(2015, 5, 30, 20, 0, 0), "Admin Dinner", 500);

    public static final UserMeal ADMIN_MEAL_4 = new UserMeal(10, LocalDateTime.of(2015, 5, 31, 10, 0, 0), "Admin Breakfast", 500);

    public static final UserMeal ADMIN_MEAL_5 = new UserMeal(11, LocalDateTime.of(2015, 5, 31, 13, 0, 0), "Admin Lunch", 1000);

    public static final UserMeal ADMIN_MEAL_6 = new UserMeal(12, LocalDateTime.of(2015, 5, 31, 20, 0, 0), "Admin Dinner", 510);

    public static final UserMeal ADMIN_MEAL_NEW = new UserMeal(13, LocalDateTime.of(2015, 5, 31, 10, 0, 0), "Admin New Meal", 550);
}
