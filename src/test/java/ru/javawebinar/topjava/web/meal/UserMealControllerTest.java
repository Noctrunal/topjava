package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class UserMealControllerTest extends AbstractControllerTest {

    private static final String MEALS_URL = "/meals";

    @Test
    public void testMealList() throws Exception {
        UserMeal expected = userMealService.get(MEAL1_ID, USER_ID);
        getMealListForSeeDifferent(expected.getId(), expected.getDateTime(), expected.getDescription(), expected.getCalories(), 6);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(get(MEALS_URL + "/delete?id=100002"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/meals"));

        getMealListForSeeDifferent(0, null, null, 0, 5);
    }

    @Test
    public void testEditForUpdate() throws Exception {
        UserMeal expected = userMealService.get(MEAL1_ID, USER_ID);
        String updateRequest = MEALS_URL + "/update?id=100002";
        getMealEditForSeeBean(expected, updateRequest);
    }

    @Test
    public void testEditForCreate() throws Exception {
        UserMeal expected = new UserMeal(LocalDateTime.now(), "", 1000);
        String createRequest = MEALS_URL + "/create";
        getMealEditForSeeBean(expected, createRequest);
    }

    private void getMealEditForSeeBean(UserMeal expected, String request) throws Exception {
        mockMvc.perform(get(request))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("mealEdit"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mealEdit.jsp"))
                .andExpect(model().attribute("meal", hasProperty("id", is(expected.getId()))))
                .andExpect(model().attribute("meal", hasProperty("description", is(expected.getDescription()))))
                .andExpect(model().attribute("meal", hasProperty("calories", is(expected.getCalories()))));
    }

    @Test
    public void testUpdateOrCreate() throws Exception {
        String requestMeals = MEALS_URL;
        Integer id = 100010;
        LocalDateTime dateTime = LocalDateTime.of(2015, 6, 1, 10, 0);
        String description = "Завтрак";
        String updateDescription = "Обновлённый завтрак";
        int calories = 300;
        int size = 7;

        //Test create
        sendCreateOrUpdate(requestMeals, 0, dateTime, description, calories);

        getMealListForSeeDifferent(id, dateTime, description, calories, size);

        //Test update
        sendCreateOrUpdate(requestMeals, id, dateTime, updateDescription, calories);

        getMealListForSeeDifferent(id, dateTime, updateDescription, calories, size);
    }

    private void sendCreateOrUpdate(String requestMeals, Integer id, LocalDateTime dateTime, String description, int calories) throws Exception {
        mockMvc.perform(post(requestMeals)
                .param("id", id != 0 ? String.valueOf(id) : "")
                .param("dateTime", dateTime.toString())
                .param("description", description)
                .param("calories", String.valueOf(calories)))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/meals"));
    }

    private void getMealListForSeeDifferent(Integer id, LocalDateTime dateTime, String description, int calories, int size) throws Exception {
        if (id != 0 && dateTime != null && description != null && calories != 0) {
            mockMvc.perform(get("/meals"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("mealList"))
                    .andExpect(forwardedUrl("/WEB-INF/jsp/mealList.jsp"))
                    .andExpect(model().attribute("mealList", hasSize(size)))
                    .andExpect(model().attribute("mealList", hasItem(
                            allOf(
                                    hasProperty("id", is(id)),
                                    hasProperty("dateTime", is(dateTime)),
                                    hasProperty("description", is(description)),
                                    hasProperty("calories", is(calories))
                            )
                    )));
        } else {
            mockMvc.perform(get("/meals"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("mealList"))
                    .andExpect(forwardedUrl("/WEB-INF/jsp/mealList.jsp"))
                    .andExpect(model().attribute("mealList", hasSize(size)));
        }
    }

    @Test
    public void testGetBetween() throws Exception {
        mockMvc.perform(post(MEALS_URL + "/filter")
               .param("startDate", "")
               .param("endDate", "")
               .param("startTime", "07:00")
               .param("endTime", "12:00"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(view().name("mealList"));
    }
}