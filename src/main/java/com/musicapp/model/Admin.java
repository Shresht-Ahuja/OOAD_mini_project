package com.musicapp.model;

import java.util.List;

/**
 * Admin extends User (Inheritance / Liskov Substitution Principle).
 * Admin IS-A User and can be used anywhere a User reference is expected.
 */
public class Admin extends User {

    public Admin() {
        super();
        setRole("ADMIN");
    }

    public Admin(int userId, String name, String email, String password, String role) {
        super(userId, name, email, password, role);
    }

    // ── Admin-only business methods ───────────────────────────────────────────

    /**
     * Adds a new artist to the platform.
     * Returns the artist for chaining / service use.
     */
    public Artist addArtist(Artist artist) {
        System.out.println("[ADMIN] Artist added: " + artist.getName());
        return artist;
    }

    /**
     * Removes an artist by reference.
     */
    public void removeArtist(List<Artist> artists, int artistId) {
        artists.removeIf(a -> a.getArtistId() == artistId);
        System.out.println("[ADMIN] Artist removed: id=" + artistId);
    }

    /**
     * Removes a song globally by id from a given list.
     */
    public void removeSong(List<Song> songs, int songId) {
        songs.removeIf(s -> s.getSongId() == songId);
        System.out.println("[ADMIN] Song removed: id=" + songId);
    }

    /**
     * Displays a summary of all users (management hook).
     */
    public void manageUsers(List<User> users) {
        System.out.println("[ADMIN] Total users on platform: " + users.size());
        users.forEach(u -> System.out.println("   - " + u.getUserId() + " | " + u.getName() + " | " + u.getRole()));
    }
}
