package com.musicapp.dto;

/** Request body for adding/editing a review. */
public class ReviewRequest {
    private int    userId;
    private int    songId;
    private int    rating;    // 1-5
    private String comment;

    public int    getUserId()              { return userId; }
    public void   setUserId(int userId)    { this.userId = userId; }
    public int    getSongId()              { return songId; }
    public void   setSongId(int songId)    { this.songId = songId; }
    public int    getRating()              { return rating; }
    public void   setRating(int rating)    { this.rating = rating; }
    public String getComment()             { return comment; }
    public void   setComment(String c)     { this.comment = c; }
}
