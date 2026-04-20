package com.musicapp.dto;

/** Request body for creating/updating an artist. */
public class ArtistRequest {
    private String name;
    private String bio;

    public String getName()              { return name; }
    public void   setName(String name)   { this.name = name; }
    public String getBio()               { return bio; }
    public void   setBio(String bio)     { this.bio = bio; }
}
