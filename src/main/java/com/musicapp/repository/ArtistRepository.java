package com.musicapp.repository;

import com.musicapp.model.Artist;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory Artist store.
 */
@Repository
public class ArtistRepository {

    private final Map<Integer, Artist> store      = new HashMap<>();
    private final AtomicInteger        idSequence = new AtomicInteger(10);

    public Artist save(Artist artist) {
        if (artist.getArtistId() == 0) {
            artist.setArtistId(idSequence.incrementAndGet());
        }
        store.put(artist.getArtistId(), artist);
        return artist;
    }

    public Optional<Artist> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Artist> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean deleteById(int id) {
        return store.remove(id) != null;
    }

    public int nextId() { return idSequence.incrementAndGet(); }
}
