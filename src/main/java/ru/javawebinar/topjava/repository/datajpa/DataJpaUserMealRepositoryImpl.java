package ru.javawebinar.topjava.repository.datajpa;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * GKislin
 * 27.03.2015.
 */
@Repository
public class DataJpaUserMealRepositoryImpl implements UserMealRepository {
    private static final Logger LOG = getLogger(DataJpaUserMealRepositoryImpl.class);
    @Autowired
    private ProxyUserMealRepository proxyUserMealRepository;

    @Autowired
    private ProxyUserRepository proxyUserRepository;

    @Override
    public UserMeal save(UserMeal userMeal, int userId) {
        User user = proxyUserRepository.findOne(userId);
        userMeal.setUser(user);
        LOG.info(userMeal.isNew() ? "Save {}" : "Update {}", userMeal);
        if (userMeal.isNew()) {
            return proxyUserMealRepository.save(userMeal);
        } else {
            if (proxyUserMealRepository.update(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), userMeal.getId(), userId) == 0) {
                return null;
            }
        }
        return userMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        LOG.info("Delete UserMeal id = {} user_id = {}", id, userId);
        return proxyUserMealRepository.delete(id, userId) != 0;
    }

    @Override
    public UserMeal get(int id, int userId) {
        LOG.info("Find UserMeal id = {} user_id = {}", id, userId);
        return proxyUserMealRepository.find(id, userId);
    }

    @Override
    public List<UserMeal> getAll(int userId) {
        LOG.info("Find all UserMeals user_id = {}", userId);
        return proxyUserMealRepository.findAll(userId);
    }

    @Override
    public List<UserMeal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        LOG.info("Find all UserMeals between startDate = {} and endDate = {} user_id = {}", startDate, endDate, userId);
        return proxyUserMealRepository.findAllBetween(startDate, endDate, userId);
    }
}