package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class UserMealRestControllerTest extends AbstractControllerTest {

    private static final String MEALS_REST_URL = UserMealRestController.MEALS_REST_URL + "/";

    @Test
    public void testGetAll() throws Exception {
        LoggedUser.setId(USER_ID);
        int userId = LoggedUser.id();
        TestUtil.print(mockMvc.perform(get(MEALS_REST_URL)
                       .contentType(MediaType.APPLICATION_JSON))
                       .andExpect(status().isOk())
                       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                       .andExpect(TO_MATCHER.contentListMatcher(UserMealsUtil.getWithExceeded(userMealService.getAll(userId), UserMealsUtil.DEFAULT_CALORIES_PER_DAY))));
    }

    @Test
    public void testGet() throws Exception {
        LoggedUser.setId(USER_ID);
        int userId = LoggedUser.id();
        mockMvc.perform(get(MEALS_REST_URL + MEAL1_ID))
                       .andExpect(status().isOk())
                       .andDo(print())
                       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                       .andExpect(MATCHER.contentMatcher(userMealService.get(MEAL1_ID, userId)));
    }

    @Test
    public void testDelete() throws Exception {
        LoggedUser.setId(ADMIN_ID);
        int adminId = LoggedUser.id();
        mockMvc.perform(delete(MEALS_REST_URL + ADMIN_MEAL_ID)
                       .contentType(MediaType.APPLICATION_JSON))
                       .andDo(print())
                       .andExpect(status().isOk());
        MATCHER.assertCollectionEquals(Collections.singletonList(ADMIN_MEAL2), userMealService.getAll(adminId));
    }

    @Test
    public void testUpdate() throws Exception {
        LoggedUser.setId(USER_ID);
        int userId = LoggedUser.id();
        UserMeal updated = MEAL1;
        updated.setDescription("Обновлённый завтрак");
        mockMvc.perform(put(MEALS_REST_URL + MEAL1_ID)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(JsonUtil.writeValue(updated)))
                       .andExpect(status().isOk());
        MATCHER.assertEquals(updated, userMealService.get(MEAL1_ID, userId));
    }

    @Test
    public void testCreateResponseEntity() throws Exception {
        LoggedUser.setId(USER_ID);
        int userId = LoggedUser.id();
        UserMeal expected = new UserMeal(LocalDateTime.of(2015, Month.JUNE, 1, 18, 0), "Созданный ужин", 300);
        ResultActions action = mockMvc.perform(post(MEALS_REST_URL)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(JsonUtil.writeValue(expected)))
                       .andExpect(status().isCreated());
        UserMeal returned = MATCHER.fromJsonAction(action);
        expected.setId(returned.getId());

        MATCHER.assertEquals(expected, returned);
        MATCHER.assertCollectionEquals(Arrays.asList(expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), userMealService.getAll(userId));
    }

    @Test
    public void testGetBetween() throws Exception {
        LoggedUser.setId(USER_ID);
        int userId = LoggedUser.id();
        TestUtil.print(mockMvc.perform(get(MEALS_REST_URL + "2015-05-30T07:00:00/2015-05-31T12:00:00")
                      .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status().isOk())
                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                      .andExpect(TO_MATCHER.contentListMatcher(UserMealsUtil.getFilteredWithExceeded(userMealService.getBetweenDates(LocalDate.of(2015, 5, 30), LocalDate.of(2015, 5, 31), userId),
                                                                                                     LocalTime.of(7, 0), LocalTime.of(12, 0), UserMealsUtil.DEFAULT_CALORIES_PER_DAY))));
    }
}