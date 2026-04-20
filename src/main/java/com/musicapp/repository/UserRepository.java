package com.musicapp.repository;

import com.musicapp.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory User store.
 * Design Pattern: Repository (Structural — enforced by Spring framework).
 * Design Principle: Single Responsibility — only handles User persistence.
 */
@Repository
public class UserRepository {

    private final Map<Integer, User> store      = new HashMap<>();
    private final AtomicInteger      idSequence = new AtomicInteger(100);

    public User save(User user) {
        if (user.getUserId() == 0) {
            user.setUserId(idSequence.incrementAndGet());
        }
        store.put(user.getUserId(), user);
        return user;
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean deleteById(int id) {
        return store.remove(id) != null;
    }

    public boolean existsByEmail(String email) {
        return store.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public int nextId() { return idSequence.incrementAndGet(); }
}
