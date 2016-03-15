package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private Map<Integer, User> repository = new ConcurrentHashMap<>();

    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
        }
        repository.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(int id) {
        if (repository.containsKey(id)) {
            repository.remove(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User get(int id) {
        if (repository.containsKey(id)) {
            return repository.get(id);
        } else {
            return null;
        }
    }

    @Override
    public User getByEmail(String email) {
        for (User user : repository.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return repository.values().stream()
                .sorted((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()))
                .collect(Collectors.toList());
    }
}
