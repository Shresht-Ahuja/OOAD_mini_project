package com.musicapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

/**
 * Core User entity.
 *
 * Design Principle — Single Responsibility:
 *   User is responsible only for identity and authentication state.
 *   Playlist/Review management is delegated to their respective services.
 *
 * Design Principle — Liskov Substitution:
 *   Admin IS-A User and can be substituted wherever User is expected.
 */
public class User {

    private int userId;
    private String name;
    private String email;
    private String password;
    private String role;          // "USER" | "ADMIN"

    @JsonIgnore
    private boolean loggedIn = false;

    // 1-to-many: User → Playlist
    @JsonIgnore
    private List<Playlist> playlists = new ArrayList<>();

    // 1-to-many: User → Review
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    // ── Constructors ──────────────────────────────────────────────────────────
    public User() {}

    public User(int userId, String name, String email, String password, String role) {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.role     = role;
    }

    // ── Business Methods ──────────────────────────────────────────────────────

    /** Validates credentials and sets loggedIn flag. */
    public boolean login(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            this.loggedIn = true;
            System.out.println("[AUTH] User '" + name + "' logged in.");
            return true;
        }
        return false;
    }

    public void logout() {
        this.loggedIn = false;
        System.out.println("[AUTH] User '" + name + "' logged out.");
    }

    public void updateProfile(String name, String email) {
        if (name  != null && !name.isBlank())  this.name  = name;
        if (email != null && !email.isBlank()) this.email = email;
        System.out.println("[PROFILE] Updated profile for userId=" + userId);
    }

    // ── Collection helpers ────────────────────────────────────────────────────
    public void addPlaylist(Playlist p)  { playlists.add(p); }
    public void addReview(Review r)      { reviews.add(r); }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public int    getUserId()                    { return userId; }
    public void   setUserId(int userId)          { this.userId = userId; }
    public String getName()                      { return name; }
    public void   setName(String name)           { this.name = name; }
    public String getEmail()                     { return email; }
    public void   setEmail(String email)         { this.email = email; }
    public String getPassword()                  { return password; }
    public void   setPassword(String password)   { this.password = password; }
    public String getRole()                      { return role; }
    public void   setRole(String role)           { this.role = role; }
    public boolean isLoggedIn()                  { return loggedIn; }
    public List<Playlist> getPlaylists()         { return playlists; }
    public List<Review>   getReviews()           { return reviews; }
}
