package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMealServiceTest {
    @Autowired
    protected UserMealService userMealService;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        UserMeal searchedMeal = userMealService.get(1, 100000);
        MATCHER.assertEquals(USER_MEAL_1, searchedMeal);
    }

    @Test
    public void testDelete() throws Exception {
        userMealService.delete(1, 100000);
        Collection<UserMeal> allMeals = userMealService.getAll(100000);
        MATCHER.assertCollectionEquals(Arrays.asList(
                USER_MEAL_6, USER_MEAL_5, USER_MEAL_4,
                USER_MEAL_3, USER_MEAL_2
        ), allMeals);
    }

    @Test
    public void testGetBetweenDates() throws Exception {
        Collection<UserMeal> mealsBetweenDates = userMealService.getBetweenDates(LocalDate.of(2015, 5, 31), LocalDate.of(2015, 5, 31), 100000);
        MATCHER.assertCollectionEquals(Arrays.asList(
                USER_MEAL_6, USER_MEAL_5, USER_MEAL_4
        ), mealsBetweenDates);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        Collection<UserMeal> mealsBetweenDateTimes = userMealService.getBetweenDateTimes(
                LocalDateTime.of(2015, 5, 30, 12, 0), LocalDateTime.of(2015, 5, 31, 12, 0), 100001);
        MATCHER.assertCollectionEquals(Arrays.asList(
                ADMIN_MEAL_4, ADMIN_MEAL_3, ADMIN_MEAL_2
        ), mealsBetweenDateTimes);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<UserMeal> allMeals = userMealService.getAll(100001);
        MATCHER.assertCollectionEquals(Arrays.asList(
                ADMIN_MEAL_6, ADMIN_MEAL_5, ADMIN_MEAL_4,
                ADMIN_MEAL_3, ADMIN_MEAL_2, ADMIN_MEAL_1
        ), allMeals);
    }

    @Test
    public void testUpdate() throws Exception {
        UserMeal mealForUpdate = userMealService.get(1, 100000);
        mealForUpdate.setCalories(450);
        mealForUpdate.setDescription("Update Meal");
        MATCHER.assertEquals(USER_MEAL_1_UPDATE, userMealService.update(mealForUpdate, 100000));
    }

    @Test
    public void testSave() throws Exception {
        UserMeal newAdminMeal = userMealService.save(new UserMeal(13, LocalDateTime.of(2015, 5, 31, 10, 0, 0), "Admin New Meal", 550), 100001);
        MATCHER.assertEquals(ADMIN_MEAL_NEW, newAdminMeal);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        userMealService.get(1, 1);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        userMealService.delete(1, 1);
    }
}