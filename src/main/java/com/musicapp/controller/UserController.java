package com.musicapp.controller;

import com.musicapp.dto.LoginRequest;
import com.musicapp.dto.RegisterRequest;
import com.musicapp.model.User;
import com.musicapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for User operations (C in MVC).
 * Base path: /api/users
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** POST /api/users/register */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        User user = userService.register(req.getName(), req.getEmail(),
                                         req.getPassword(), req.getRole());
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already registered"));
        }
        return ResponseEntity.ok(user);
    }

    /** POST /api/users/login */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userService.login(req.getEmail(), req.getPassword());
        if (user == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid credentials"));
        }
        return ResponseEntity.ok(user);
    }

    /** GET /api/users */
    @GetMapping
    public ResponseEntity<List<User>> listAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    /** GET /api/users/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return userService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** PUT /api/users/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @RequestBody Map<String, String> body) {
        User updated = userService.updateProfile(id, body.get("name"), body.get("email"));
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/users/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }
}
