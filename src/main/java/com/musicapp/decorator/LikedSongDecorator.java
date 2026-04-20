package com.musicapp.decorator;

import com.musicapp.model.Song;

/**
 * Concrete Decorator — adds a "❤ LIKED" badge to the song display label.
 *
 * Design Pattern: Decorator (Structural)
 */
public class LikedSongDecorator extends SongDecorator {

    public LikedSongDecorator(Song song) {
        super(song);
    }

    @Override
    public String getDisplayLabel() {
        String base = wrappedSong.getTitle()
                + " (" + wrappedSong.formatDuration() + ")"
                + " — " + wrappedSong.getGenre();
        return wrappedSong.isLiked() ? "❤ " + base : base;
    }
}
