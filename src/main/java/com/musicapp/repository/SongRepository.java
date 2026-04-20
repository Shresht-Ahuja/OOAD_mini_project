package com.musicapp.repository;

import com.musicapp.model.Song;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In-memory Song store.
 */
@Repository
public class SongRepository {

    private final Map<Integer, Song> store      = new HashMap<>();
    private final AtomicInteger      idSequence = new AtomicInteger(10);

    public Song save(Song song) {
        if (song.getSongId() == 0) {
            song.setSongId(idSequence.incrementAndGet());
        }
        store.put(song.getSongId(), song);
        return song;
    }

    public Optional<Song> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Song> findAll() {
        return new ArrayList<>(store.values());
    }

    public List<Song> findByGenre(String genre) {
        return store.values().stream()
                .filter(s -> s.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public List<Song> searchByTitle(String keyword) {
        String lower = keyword.toLowerCase();
        return store.values().stream()
                .filter(s -> s.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public boolean deleteById(int id) {
        return store.remove(id) != null;
    }

    public int nextId() { return idSequence.incrementAndGet(); }
}
