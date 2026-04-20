package com.musicapp.service;

import com.musicapp.model.Artist;
import com.musicapp.model.Song;
import com.musicapp.observer.ActivityLogListener;
import com.musicapp.repository.ArtistRepository;
import com.musicapp.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for Song management.
 *
 * Wires the Observer pattern: every song gets the ActivityLogListener
 * attached when created so that play/pause/like events are logged.
 */
@Service
public class SongService {

    private final SongRepository     songRepository;
    private final ArtistRepository   artistRepository;
    private final ActivityLogListener activityLog;

    @Autowired
    public SongService(SongRepository songRepository,
                       ArtistRepository artistRepository,
                       ActivityLogListener activityLog) {
        this.songRepository  = songRepository;
        this.artistRepository = artistRepository;
        this.activityLog     = activityLog;
    }

    public Song uploadSong(int artistId, String title, double duration,
                           String genre, String filePath) {
        int id = songRepository.nextId();
        Song song = new Song(id, title, duration, genre, filePath);
        song.addListener(activityLog);           // wire observer
        Song saved = songRepository.save(song);
        artistRepository.findById(artistId).ifPresent(a -> a.uploadSong(saved));
        return saved;
    }

    public Optional<Song> findById(int id) {
        return songRepository.findById(id);
    }

    public List<Song> findAll() {
        return songRepository.findAll();
    }

    public List<Song> searchByTitle(String keyword) {
        return songRepository.searchByTitle(keyword);
    }

    public List<Song> findByGenre(String genre) {
        return songRepository.findByGenre(genre);
    }

    public String playSong(int songId) {
        return songRepository.findById(songId)
                .map(s -> {
                    s.addListener(activityLog);
                    return s.play();
                })
                .orElse("Song not found");
    }

    public String pauseSong(int songId) {
        return songRepository.findById(songId)
                .map(s -> {
                    s.addListener(activityLog);
                    return s.pause();
                })
                .orElse("Song not found");
    }

    public String likeSong(int songId) {
        return songRepository.findById(songId)
                .map(s -> {
                    s.addListener(activityLog);
                    return s.like();
                })
                .orElse("Song not found");
    }

    public boolean deleteSong(int id) {
        return songRepository.deleteById(id);
    }
}
