package com.musicapp.repository;

import com.musicapp.model.Album;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory Album store.
 */
@Repository
public class AlbumRepository {

    private final Map<Integer, Album> store      = new HashMap<>();
    private final AtomicInteger       idSequence = new AtomicInteger(10);

    public Album save(Album album) {
        if (album.getAlbumId() == 0) {
            album.setAlbumId(idSequence.incrementAndGet());
        }
        store.put(album.getAlbumId(), album);
        return album;
    }

    public Optional<Album> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Album> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean deleteById(int id) {
        return store.remove(id) != null;
    }

    public int nextId() { return idSequence.incrementAndGet(); }
}
