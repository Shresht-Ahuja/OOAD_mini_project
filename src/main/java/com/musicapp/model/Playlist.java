package com.musicapp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Playlist entity.
 *
 * Relationship:
 *   Playlist → Song (many-to-many via List<Song>)
 *   User     → Playlist (1-to-many, managed via User.playlists)
 */
public class Playlist {

    private int       playlistId;
    private String    name;
    private LocalDate createdDate;
    private int       ownerId;        // FK → User.userId

    // Many-to-many: Playlist ↔ Song
    private List<Song> songs = new ArrayList<>();

    // ── Constructors ──────────────────────────────────────────────────────────
    public Playlist() {}

    public Playlist(int playlistId, String name, int ownerId) {
        this.playlistId  = playlistId;
        this.name        = name;
        this.ownerId     = ownerId;
        this.createdDate = LocalDate.now();
    }

    // ── Business Methods ──────────────────────────────────────────────────────

    /** Adds a song if not already present (set semantics). */
    public boolean addSong(Song song) {
        boolean alreadyPresent = songs.stream().anyMatch(s -> s.getSongId() == song.getSongId());
        if (!alreadyPresent) {
            songs.add(song);
            System.out.println("[PLAYLIST] '" + song.getTitle() + "' added to playlist '" + name + "'.");
            return true;
        }
        return false;
    }

    /** Removes a song by songId. */
    public boolean removeSong(int songId) {
        boolean removed = songs.removeIf(s -> s.getSongId() == songId);
        if (removed) System.out.println("[PLAYLIST] Song id=" + songId + " removed from playlist '" + name + "'.");
        return removed;
    }

    /** Shuffles the playlist order in place. */
    public List<Song> shuffle() {
        Collections.shuffle(songs);
        System.out.println("[PLAYLIST] Playlist '" + name + "' shuffled.");
        return songs;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public int       getPlaylistId()                     { return playlistId; }
    public void      setPlaylistId(int playlistId)       { this.playlistId = playlistId; }
    public String    getName()                           { return name; }
    public void      setName(String name)                { this.name = name; }
    public LocalDate getCreatedDate()                    { return createdDate; }
    public void      setCreatedDate(LocalDate date)      { this.createdDate = date; }
    public int       getOwnerId()                        { return ownerId; }
    public void      setOwnerId(int ownerId)             { this.ownerId = ownerId; }
    public List<Song> getSongs()                         { return songs; }
}
