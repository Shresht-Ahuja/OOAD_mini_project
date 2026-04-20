package com.musicapp.service;

import com.musicapp.model.Artist;
import com.musicapp.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for Artist management.
 */
@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Artist addArtist(String name, String bio) {
        int id = artistRepository.nextId();
        Artist artist = new Artist(id, name, bio);
        return artistRepository.save(artist);
    }

    public Optional<Artist> findById(int id) {
        return artistRepository.findById(id);
    }

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public boolean removeArtist(int id) {
        return artistRepository.deleteById(id);
    }

    public Artist updateArtist(int id, String name, String bio) {
        return artistRepository.findById(id).map(a -> {
            if (name != null && !name.isBlank()) a.setName(name);
            if (bio  != null && !bio.isBlank())  a.setBio(bio);
            return artistRepository.save(a);
        }).orElse(null);
    }
}
