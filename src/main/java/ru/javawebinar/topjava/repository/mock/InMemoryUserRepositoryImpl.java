package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private Map<Integer, User> userMap = new ConcurrentHashMap<>();

    private AtomicInteger generateId = new AtomicInteger(0);

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(generateId.incrementAndGet());
        }
        userMap.put(user.getId(), user);
        return userMap.get(user.getId());
    }

    @Override
    public boolean delete(int id) {
        User user = userMap.remove(id);
        return !(userMap.containsValue(user));
    }

    @Override
    public User get(int id) {
        return userMap.get(id);
    }

    @Override
    public User getByEmail(String email) {
        for (User user : userMap.values()) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return  new ArrayList<>(userMap.values()).stream()
                .sorted((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()))
                .collect(Collectors.toList());
    }
}
