package com.musicapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.musicapp.observer.SongEvent;
import com.musicapp.observer.SongEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Song entity.
 *
 * Design Pattern — Observer (Behavioral):
 *   Song acts as the subject/publisher. It maintains a list of
 *   SongEventListeners and notifies them on play, pause, and like events.
 *
 * Design Principle — Open/Closed:
 *   Song behaviour is extended via the Decorator pattern (SongDecorator)
 *   without modifying this class.
 *
 * Relationship:
 *   Song → Review (1-to-many)
 */
public class Song {

    private int    songId;
    private String title;
    private double duration;   // seconds
    private String genre;
    private String filePath;

    private int     likeCount  = 0;
    private boolean isPlaying  = false;
    private boolean isLiked    = false;

    // 1-to-many: Song → Review
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    // Observer pattern: list of listeners
    @JsonIgnore
    private final List<SongEventListener> listeners = new ArrayList<>();

    // ── Constructors ──────────────────────────────────────────────────────────
    public Song() {}

    public Song(int songId, String title, double duration, String genre, String filePath) {
        this.songId   = songId;
        this.title    = title;
        this.duration = duration;
        this.genre    = genre;
        this.filePath = filePath;
    }

    // ── Observer management ───────────────────────────────────────────────────
    public void addListener(SongEventListener listener)    { listeners.add(listener); }
    public void removeListener(SongEventListener listener) { listeners.remove(listener); }

    private void notifyListeners(SongEvent event) {
        listeners.forEach(l -> l.onSongEvent(event, this));
    }

    // ── Business Methods ──────────────────────────────────────────────────────

    /** Simulates playing the song and notifies observers. */
    public String play() {
        isPlaying = true;
        String msg = "▶ Playing: '" + title + "' [" + formatDuration() + "]";
        System.out.println("[SONG] " + msg);
        notifyListeners(SongEvent.PLAY);
        return msg;
    }

    /** Simulates pausing the song and notifies observers. */
    public String pause() {
        isPlaying = false;
        String msg = "⏸ Paused: '" + title + "'";
        System.out.println("[SONG] " + msg);
        notifyListeners(SongEvent.PAUSE);
        return msg;
    }

    /** Toggles like status and notifies observers. */
    public String like() {
        isLiked = !isLiked;
        if (isLiked) likeCount++; else likeCount--;
        String msg = (isLiked ? "❤ Liked" : "🤍 Unliked") + ": '" + title + "' (total likes: " + likeCount + ")";
        System.out.println("[SONG] " + msg);
        notifyListeners(SongEvent.LIKE);
        return msg;
    }

    /** Formats duration as mm:ss */
    public String formatDuration() {
        int mins = (int) duration / 60;
        int secs = (int) duration % 60;
        return String.format("%d:%02d", mins, secs);
    }

    public void addReview(Review r)    { reviews.add(r); }
    public void removeReview(int id)   { reviews.removeIf(r -> r.getReviewId() == id); }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public int     getSongId()                   { return songId; }
    public void    setSongId(int songId)         { this.songId = songId; }
    public String  getTitle()                    { return title; }
    public void    setTitle(String title)        { this.title = title; }
    public double  getDuration()                 { return duration; }
    public void    setDuration(double duration)  { this.duration = duration; }
    public String  getGenre()                    { return genre; }
    public void    setGenre(String genre)        { this.genre = genre; }
    public String  getFilePath()                 { return filePath; }
    public void    setFilePath(String filePath)  { this.filePath = filePath; }
    public int     getLikeCount()                { return likeCount; }
    public boolean isPlaying()                   { return isPlaying; }
    public boolean isLiked()                     { return isLiked; }
    public List<Review> getReviews()             { return reviews; }
}
