package com.musicapp.service;

import com.musicapp.model.Album;
import com.musicapp.model.Artist;
import com.musicapp.model.Song;
import com.musicapp.repository.AlbumRepository;
import com.musicapp.repository.ArtistRepository;
import com.musicapp.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for Album management.
 */
@Service
public class AlbumService {

    private final AlbumRepository  albumRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository   songRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository,
                        ArtistRepository artistRepository,
                        SongRepository songRepository) {
        this.albumRepository  = albumRepository;
        this.artistRepository = artistRepository;
        this.songRepository   = songRepository;
    }

    public Album createAlbum(int artistId, String title, LocalDate releaseDate) {
        int id = albumRepository.nextId();
        Album album = new Album(id, title, releaseDate);
        Album saved = albumRepository.save(album);
        // register the album under the artist
        artistRepository.findById(artistId).ifPresent(a -> a.createAlbum(saved));
        return saved;
    }

    public Optional<Album> findById(int id) {
        return albumRepository.findById(id);
    }

    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    public boolean addSongToAlbum(int albumId, int songId) {
        Optional<Album> album = albumRepository.findById(albumId);
        Optional<Song>  song  = songRepository.findById(songId);
        if (album.isPresent() && song.isPresent()) {
            album.get().addSong(song.get());
            albumRepository.save(album.get());
            return true;
        }
        return false;
    }

    public boolean removeSongFromAlbum(int albumId, int songId) {
        return albumRepository.findById(albumId)
                .map(a -> {
                    boolean removed = a.removeSong(songId);
                    if (removed) albumRepository.save(a);
                    return removed;
                }).orElse(false);
    }

    public boolean deleteAlbum(int id) {
        return albumRepository.deleteById(id);
    }
}
