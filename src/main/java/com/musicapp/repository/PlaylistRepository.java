package com.musicapp.repository;

import com.musicapp.model.Playlist;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In-memory Playlist store.
 */
@Repository
public class PlaylistRepository {

    private final Map<Integer, Playlist> store      = new HashMap<>();
    private final AtomicInteger          idSequence = new AtomicInteger(10);

    public Playlist save(Playlist playlist) {
        if (playlist.getPlaylistId() == 0) {
            playlist.setPlaylistId(idSequence.incrementAndGet());
        }
        store.put(playlist.getPlaylistId(), playlist);
        return playlist;
    }

    public Optional<Playlist> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Playlist> findAll() {
        return new ArrayList<>(store.values());
    }

    public List<Playlist> findByOwnerId(int userId) {
        return store.values().stream()
                .filter(p -> p.getOwnerId() == userId)
                .collect(Collectors.toList());
    }

    public boolean deleteById(int id) {
        return store.remove(id) != null;
    }

    public int nextId() { return idSequence.incrementAndGet(); }
}
