package com.musicapp.controller;

import com.musicapp.dto.SongRequest;
import com.musicapp.model.Song;
import com.musicapp.observer.ActivityLogListener;
import com.musicapp.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Song operations.
 * Base path: /api/songs
 */
@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*")
public class SongController {

    private final SongService         songService;
    private final ActivityLogListener activityLog;

    @Autowired
    public SongController(SongService songService,
                          ActivityLogListener activityLog) {
        this.songService  = songService;
        this.activityLog  = activityLog;
    }

    /** GET /api/songs */
    @GetMapping
    public ResponseEntity<List<Song>> listAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(songService.searchByTitle(search));
        }
        if (genre != null && !genre.isBlank()) {
            return ResponseEntity.ok(songService.findByGenre(genre));
        }
        return ResponseEntity.ok(songService.findAll());
    }

    /** GET /api/songs/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return songService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/songs */
    @PostMapping
    public ResponseEntity<Song> upload(@RequestBody SongRequest req) {
        Song song = songService.uploadSong(req.getArtistId(), req.getTitle(),
                                           req.getDuration(), req.getGenre(),
                                           req.getFilePath());
        return ResponseEntity.ok(song);
    }

    /** POST /api/songs/{id}/play */
    @PostMapping("/{id}/play")
    public ResponseEntity<?> play(@PathVariable int id) {
        String result = songService.playSong(id);
        return ResponseEntity.ok(Map.of("status", result));
    }

    /** POST /api/songs/{id}/pause */
    @PostMapping("/{id}/pause")
    public ResponseEntity<?> pause(@PathVariable int id) {
        String result = songService.pauseSong(id);
        return ResponseEntity.ok(Map.of("status", result));
    }

    /** POST /api/songs/{id}/like */
    @PostMapping("/{id}/like")
    public ResponseEntity<?> like(@PathVariable int id) {
        String result = songService.likeSong(id);
        return ResponseEntity.ok(Map.of("status", result));
    }

    /** DELETE /api/songs/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        boolean deleted = songService.deleteSong(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "Song deleted"));
    }

    /** GET /api/songs/activity-log */
    @GetMapping("/activity-log")
    public ResponseEntity<List<String>> activityLog() {
        return ResponseEntity.ok(activityLog.getActivityLog());
    }
}
