package com.musicapp.decorator;

import com.musicapp.model.Song;

/**
 * Decorator (Structural) Design Pattern — base component wrapper.
 *
 * Wraps a Song and delegates all calls by default.
 * Concrete decorators extend this class to add behaviour without
 * modifying the Song class (Open/Closed Principle).
 */
public abstract class SongDecorator {

    protected final Song wrappedSong;

    public SongDecorator(Song song) {
        this.wrappedSong = song;
    }

    public int    getSongId()        { return wrappedSong.getSongId(); }
    public String getTitle()         { return wrappedSong.getTitle(); }
    public double getDuration()      { return wrappedSong.getDuration(); }
    public String getGenre()         { return wrappedSong.getGenre(); }
    public String getFilePath()      { return wrappedSong.getFilePath(); }
    public int    getLikeCount()     { return wrappedSong.getLikeCount(); }
    public boolean isPlaying()       { return wrappedSong.isPlaying(); }
    public boolean isLiked()         { return wrappedSong.isLiked(); }

    /** Returns a display-ready label — concrete decorators override this. */
    public abstract String getDisplayLabel();
}
