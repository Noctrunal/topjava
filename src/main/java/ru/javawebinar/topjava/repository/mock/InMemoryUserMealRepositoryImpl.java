package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.09.2015.
 */

@Repository
public class InMemoryUserMealRepositoryImpl implements UserMealRepository {
    private Map<Integer, Map<Integer, UserMeal>> mealsFromCurrentUser = new ConcurrentHashMap<>();

    private Map<Integer, UserMeal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        UserMealsUtil.MEAL_LIST.forEach(e -> {
            this.save(e, 1);
        });
    }

    @Override
    public UserMeal save(UserMeal userMeal, int userId) {
        if (userMeal.isNew()) {
            userMeal.setId(counter.incrementAndGet());
        }
        repository.put(userMeal.getId(), userMeal);
        mealsFromCurrentUser.put(userId, repository);
        return userMeal;
    }

    @Override
    public void delete(int id, int userId) {
        if (!mealsFromCurrentUser.containsKey(userId)) {
            throw new NotFoundException("Not found meal for user with ID: " + userId);
        }

        mealsFromCurrentUser.get(userId).remove(id);
    }

    @Override
    public UserMeal get(int id, int userId) {
        if (!mealsFromCurrentUser.containsKey(userId)) {
            throw new NotFoundException("Not found meal for user with ID: " + userId);
        }
        return mealsFromCurrentUser.get(userId).get(id);
    }

    @Override
    public Collection<UserMeal> getAll(int userId) {

        if (!mealsFromCurrentUser.containsKey(userId)) {
            throw new NotFoundException("Not found meals for user with ID: " + userId);
        }

        return mealsFromCurrentUser.get(userId).values().stream()
                .sorted((e1, e2) -> e1.getDateTime().toLocalDate().compareTo(e2.getDateTime().toLocalDate()))
                .collect(Collectors.toList());
    }
}

