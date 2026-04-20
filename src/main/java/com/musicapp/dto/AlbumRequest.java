package com.musicapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

/** Request body for creating an album. */
public class AlbumRequest {
    private int    artistId;
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    public int       getArtistId()                     { return artistId; }
    public void      setArtistId(int artistId)         { this.artistId = artistId; }
    public String    getTitle()                        { return title; }
    public void      setTitle(String title)            { this.title = title; }
    public LocalDate getReleaseDate()                  { return releaseDate; }
    public void      setReleaseDate(LocalDate d)       { this.releaseDate = d; }
}
