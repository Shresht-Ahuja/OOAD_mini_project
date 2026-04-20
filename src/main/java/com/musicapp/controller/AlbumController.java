package com.musicapp.controller;

import com.musicapp.dto.AlbumRequest;
import com.musicapp.model.Album;
import com.musicapp.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Album operations.
 * Base path: /api/albums
 */
@RestController
@RequestMapping("/api/albums")
@CrossOrigin(origins = "*")
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    /** GET /api/albums */
    @GetMapping
    public ResponseEntity<List<Album>> listAll() {
        return ResponseEntity.ok(albumService.findAll());
    }

    /** GET /api/albums/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return albumService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/albums */
    @PostMapping
    public ResponseEntity<Album> create(@RequestBody AlbumRequest req) {
        Album album = albumService.createAlbum(req.getArtistId(),
                                               req.getTitle(),
                                               req.getReleaseDate());
        return ResponseEntity.ok(album);
    }

    /** POST /api/albums/{id}/songs/{songId} — add song to album */
    @PostMapping("/{id}/songs/{songId}")
    public ResponseEntity<?> addSong(@PathVariable int id,
                                     @PathVariable int songId) {
        boolean ok = albumService.addSongToAlbum(id, songId);
        if (!ok) return ResponseEntity.badRequest()
                .body(Map.of("error", "Album or Song not found"));
        return ResponseEntity.ok(Map.of("message", "Song added to album"));
    }

    /** DELETE /api/albums/{id}/songs/{songId} — remove song from album */
    @DeleteMapping("/{id}/songs/{songId}")
    public ResponseEntity<?> removeSong(@PathVariable int id,
                                        @PathVariable int songId) {
        boolean ok = albumService.removeSongFromAlbum(id, songId);
        if (!ok) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "Song removed from album"));
    }

    /** DELETE /api/albums/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        boolean deleted = albumService.deleteAlbum(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "Album deleted"));
    }
}
