package com.musicapp.service;

import com.musicapp.model.Playlist;
import com.musicapp.model.Song;
import com.musicapp.model.User;
import com.musicapp.repository.PlaylistRepository;
import com.musicapp.repository.SongRepository;
import com.musicapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for Playlist management.
 */
@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository     songRepository;
    private final UserRepository     userRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository,
                           SongRepository songRepository,
                           UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository     = songRepository;
        this.userRepository     = userRepository;
    }

    public Playlist createPlaylist(int userId, String name) {
        int id = playlistRepository.nextId();
        Playlist playlist = new Playlist(id, name, userId);
        Playlist saved = playlistRepository.save(playlist);
        userRepository.findById(userId).ifPresent(u -> u.addPlaylist(saved));
        return saved;
    }

    public Optional<Playlist> findById(int id) {
        return playlistRepository.findById(id);
    }

    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }

    public List<Playlist> findByUser(int userId) {
        return playlistRepository.findByOwnerId(userId);
    }

    public boolean addSong(int playlistId, int songId) {
        Optional<Playlist> pl   = playlistRepository.findById(playlistId);
        Optional<Song>     song = songRepository.findById(songId);
        if (pl.isPresent() && song.isPresent()) {
            boolean added = pl.get().addSong(song.get());
            if (added) playlistRepository.save(pl.get());
            return added;
        }
        return false;
    }

    public boolean removeSong(int playlistId, int songId) {
        return playlistRepository.findById(playlistId).map(pl -> {
            boolean removed = pl.removeSong(songId);
            if (removed) playlistRepository.save(pl);
            return removed;
        }).orElse(false);
    }

    public List<Song> shuffle(int playlistId) {
        return playlistRepository.findById(playlistId)
                .map(Playlist::shuffle)
                .orElse(List.of());
    }

    public boolean deletePlaylist(int id) {
        return playlistRepository.deleteById(id);
    }
}
