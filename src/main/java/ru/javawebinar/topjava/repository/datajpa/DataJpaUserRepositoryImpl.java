package ru.javawebinar.topjava.repository.datajpa;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * GKislin
 * 27.03.2015.
 */

@Repository
public class DataJpaUserRepositoryImpl implements UserRepository {
    private static final Logger LOG = getLogger(DataJpaUserRepositoryImpl.class);

    private static final Sort SORT_NAME_EMAIL = new Sort("name", "email");

    @Autowired
    private ProxyUserRepository proxyUserRepository;

    @Autowired
    private ProxyUserMealRepository proxyUserMealRepository;

    @Override
    public User save(User user) {
        return proxyUserRepository.save(user);
    }

    @Override
    public boolean delete(int id) {
        return proxyUserRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return proxyUserRepository.findOne(id);
    }

    @Override
    public User getByEmail(String email) {
        return proxyUserRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return proxyUserRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    public Map<User, List<UserMeal>> findWithMeals(int id) {
        Map<User, List<UserMeal>> findMap = new ConcurrentHashMap<>();
        User user = get(id);
        List<UserMeal> meals = proxyUserMealRepository.findAll(user.getId());
        findMap.putIfAbsent(user, meals);
        LOG.info("Find User id = {} with UserMeals = {}", id, meals);
        return findMap;
    }
}
