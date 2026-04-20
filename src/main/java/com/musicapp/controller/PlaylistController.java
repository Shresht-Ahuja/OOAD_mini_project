package com.musicapp.controller;

import com.musicapp.dto.PlaylistRequest;
import com.musicapp.model.Playlist;
import com.musicapp.model.Song;
import com.musicapp.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Playlist operations.
 * Base path: /api/playlists
 */
@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "*")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    /** GET /api/playlists */
    @GetMapping
    public ResponseEntity<List<Playlist>> listAll() {
        return ResponseEntity.ok(playlistService.findAll());
    }

    /** GET /api/playlists/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return playlistService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** GET /api/playlists/user/{userId} */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Playlist>> getByUser(@PathVariable int userId) {
        return ResponseEntity.ok(playlistService.findByUser(userId));
    }

    /** POST /api/playlists */
    @PostMapping
    public ResponseEntity<Playlist> create(@RequestBody PlaylistRequest req) {
        Playlist pl = playlistService.createPlaylist(req.getUserId(), req.getName());
        return ResponseEntity.ok(pl);
    }

    /** POST /api/playlists/{id}/songs/{songId} */
    @PostMapping("/{id}/songs/{songId}")
    public ResponseEntity<?> addSong(@PathVariable int id,
                                     @PathVariable int songId) {
        boolean ok = playlistService.addSong(id, songId);
        if (!ok) return ResponseEntity.badRequest()
                .body(Map.of("error", "Playlist or Song not found / already added"));
        return ResponseEntity.ok(Map.of("message", "Song added to playlist"));
    }

    /** DELETE /api/playlists/{id}/songs/{songId} */
    @DeleteMapping("/{id}/songs/{songId}")
    public ResponseEntity<?> removeSong(@PathVariable int id,
                                        @PathVariable int songId) {
        boolean ok = playlistService.removeSong(id, songId);
        if (!ok) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "Song removed from playlist"));
    }

    /** POST /api/playlists/{id}/shuffle */
    @PostMapping("/{id}/shuffle")
    public ResponseEntity<List<Song>> shuffle(@PathVariable int id) {
        List<Song> shuffled = playlistService.shuffle(id);
        return ResponseEntity.ok(shuffled);
    }

    /** DELETE /api/playlists/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        boolean deleted = playlistService.deletePlaylist(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "Playlist deleted"));
    }
}
