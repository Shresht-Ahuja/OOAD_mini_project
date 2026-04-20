package com.musicapp.observer;

import com.musicapp.model.Song;

/**
 * Observer interface for the Observer (Behavioral) Design Pattern.
 *
 * Any class that wants to receive song events must implement this interface.
 */
public interface SongEventListener {
    void onSongEvent(SongEvent event, Song song);
}
