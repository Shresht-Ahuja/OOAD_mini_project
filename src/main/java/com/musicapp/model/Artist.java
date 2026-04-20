package com.musicapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Artist entity.
 *
 * Relationship:
 *   Artist → Album  (1-to-many)
 *   Artist → Song   (1-to-many)
 */
public class Artist {

    private int    artistId;
    private String name;
    private String bio;

    // 1-to-many: Artist → Album
    private List<Album> albums = new ArrayList<>();

    // 1-to-many: Artist → Song
    private List<Song> songs = new ArrayList<>();

    // ── Constructors ──────────────────────────────────────────────────────────
    public Artist() {}

    public Artist(int artistId, String name, String bio) {
        this.artistId = artistId;
        this.name     = name;
        this.bio      = bio;
    }

    // ── Business Methods ──────────────────────────────────────────────────────

    /** Creates and registers an album under this artist. */
    public Album createAlbum(Album album) {
        albums.add(album);
        System.out.println("[ARTIST] '" + name + "' created album: " + album.getTitle());
        return album;
    }

    /** Uploads a song and associates it with this artist. */
    public Song uploadSong(Song song) {
        songs.add(song);
        System.out.println("[ARTIST] '" + name + "' uploaded song: " + song.getTitle());
        return song;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public int    getArtistId()                  { return artistId; }
    public void   setArtistId(int artistId)      { this.artistId = artistId; }
    public String getName()                      { return name; }
    public void   setName(String name)           { this.name = name; }
    public String getBio()                       { return bio; }
    public void   setBio(String bio)             { this.bio = bio; }
    public List<Album> getAlbums()               { return albums; }
    public List<Song>  getSongs()                { return songs; }
}
