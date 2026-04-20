package com.musicapp.observer;

import com.musicapp.model.Song;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Observer — logs every song event to an in-memory activity log.
 *
 * Design Pattern: Observer (Behavioral)
 * This bean is registered as a Spring component and can be injected wherever
 * activity logs need to be read (e.g. by a controller).
 */
@Component
public class ActivityLogListener implements SongEventListener {

    private final List<String> activityLog = new ArrayList<>();

    @Override
    public void onSongEvent(SongEvent event, Song song) {
        String entry = String.format("[%s] Event=%-5s | Song='%s' (id=%d) | likes=%d",
                LocalDateTime.now(), event.name(), song.getTitle(), song.getSongId(), song.getLikeCount());
        activityLog.add(entry);
        System.out.println("[OBSERVER] " + entry);
    }

    public List<String> getActivityLog() {
        return new ArrayList<>(activityLog);
    }

    public void clearLog() {
        activityLog.clear();
    }
}
