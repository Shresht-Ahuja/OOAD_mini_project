package com.musicapp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Album entity.
 *
 * Relationship:
 *   Album → Song (1-to-many)
 */
public class Album {

    private int       albumId;
    private String    title;
    private LocalDate releaseDate;

    // 1-to-many: Album → Song
    private List<Song> songs = new ArrayList<>();

    // ── Constructors ──────────────────────────────────────────────────────────
    public Album() {}

    public Album(int albumId, String title, LocalDate releaseDate) {
        this.albumId     = albumId;
        this.title       = title;
        this.releaseDate = releaseDate;
    }

    // ── Business Methods ──────────────────────────────────────────────────────

    /** Adds a song to this album. */
    public void addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
            System.out.println("[ALBUM] Song '" + song.getTitle() + "' added to album '" + title + "'.");
        }
    }

    /** Removes a song from this album by songId. */
    public boolean removeSong(int songId) {
        boolean removed = songs.removeIf(s -> s.getSongId() == songId);
        if (removed) System.out.println("[ALBUM] Song id=" + songId + " removed from album '" + title + "'.");
        return removed;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public int       getAlbumId()                     { return albumId; }
    public void      setAlbumId(int albumId)          { this.albumId = albumId; }
    public String    getTitle()                       { return title; }
    public void      setTitle(String title)           { this.title = title; }
    public LocalDate getReleaseDate()                 { return releaseDate; }
    public void      setReleaseDate(LocalDate date)   { this.releaseDate = date; }
    public List<Song> getSongs()                      { return songs; }
}
