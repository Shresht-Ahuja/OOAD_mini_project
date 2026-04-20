package com.musicapp.service;

import com.musicapp.factory.UserFactory;
import com.musicapp.model.User;
import com.musicapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for User management.
 *
 * Design Principle — Dependency Inversion:
 *   This service depends on the UserRepository abstraction, not a concrete
 *   storage implementation. Swap the repository for a DB-backed one with zero
 *   changes here.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user via the Factory Method pattern.
     * Returns null if the email is already taken.
     */
    public User register(String name, String email, String password, String role) {
        if (userRepository.existsByEmail(email)) {
            return null;  // email already in use
        }
        int id = userRepository.nextId();
        User user = UserFactory.createUser(id, name, email, password, role);
        return userRepository.save(user);
    }

    /**
     * Validates credentials and returns the authenticated user, or null.
     */
    public User login(String email, String password) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isPresent()) {
            User u = opt.get();
            boolean ok = u.login(email, password);
            return ok ? u : null;
        }
        return null;
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User updateProfile(int id, String name, String email) {
        return userRepository.findById(id).map(u -> {
            u.updateProfile(name, email);
            return userRepository.save(u);
        }).orElse(null);
    }

    public boolean deleteUser(int id) {
        return userRepository.deleteById(id);
    }
}
