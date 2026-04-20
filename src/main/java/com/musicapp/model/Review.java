package com.musicapp.model;

import java.time.LocalDateTime;

/**
 * Review entity.
 *
 * Relationship:
 *   User → Review (1-to-many)
 *   Song → Review (1-to-many)
 */
public class Review {

    private int           reviewId;
    private int           rating;      // 1–5
    private String        comment;
    private int           userId;      // FK → User.userId
    private int           songId;      // FK → Song.songId
    private LocalDateTime createdAt;
    private boolean       deleted = false;

    // ── Constructors ──────────────────────────────────────────────────────────
    public Review() {}

    public Review(int reviewId, int rating, String comment, int userId, int songId) {
        this.reviewId  = reviewId;
        this.rating    = rating;
        this.comment   = comment;
        this.userId    = userId;
        this.songId    = songId;
        this.createdAt = LocalDateTime.now();
    }

    // ── Business Methods ──────────────────────────────────────────────────────

    /** Edits rating and/or comment of this review. */
    public void editReview(int newRating, String newComment) {
        if (newRating >= 1 && newRating <= 5) this.rating = newRating;
        if (newComment != null && !newComment.isBlank()) this.comment = newComment;
        System.out.println("[REVIEW] Review id=" + reviewId + " updated.");
    }

    /** Soft-deletes this review. */
    public void deleteReview() {
        this.deleted = true;
        System.out.println("[REVIEW] Review id=" + reviewId + " deleted.");
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public int           getReviewId()                  { return reviewId; }
    public void          setReviewId(int reviewId)      { this.reviewId = reviewId; }
    public int           getRating()                    { return rating; }
    public void          setRating(int rating)          { this.rating = rating; }
    public String        getComment()                   { return comment; }
    public void          setComment(String comment)     { this.comment = comment; }
    public int           getUserId()                    { return userId; }
    public void          setUserId(int userId)          { this.userId = userId; }
    public int           getSongId()                    { return songId; }
    public void          setSongId(int songId)          { this.songId = songId; }
    public LocalDateTime getCreatedAt()                 { return createdAt; }
    public boolean       isDeleted()                    { return deleted; }
}
