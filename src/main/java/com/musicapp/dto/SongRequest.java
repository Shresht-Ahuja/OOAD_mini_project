package com.musicapp.dto;

/** Request body for uploading a song. */
public class SongRequest {
    private int    artistId;
    private String title;
    private double duration;
    private String genre;
    private String filePath;

    public int    getArtistId()                  { return artistId; }
    public void   setArtistId(int artistId)      { this.artistId = artistId; }
    public String getTitle()                     { return title; }
    public void   setTitle(String title)         { this.title = title; }
    public double getDuration()                  { return duration; }
    public void   setDuration(double duration)   { this.duration = duration; }
    public String getGenre()                     { return genre; }
    public void   setGenre(String genre)         { this.genre = genre; }
    public String getFilePath()                  { return filePath; }
    public void   setFilePath(String filePath)   { this.filePath = filePath; }
}
