package com.musicapp.controller;

import com.musicapp.dto.ArtistRequest;
import com.musicapp.model.Artist;
import com.musicapp.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Artist operations.
 * Base path: /api/artists
 */
@RestController
@RequestMapping("/api/artists")
@CrossOrigin(origins = "*")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    /** GET /api/artists */
    @GetMapping
    public ResponseEntity<List<Artist>> listAll() {
        return ResponseEntity.ok(artistService.findAll());
    }

    /** GET /api/artists/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return artistService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/artists */
    @PostMapping
    public ResponseEntity<Artist> create(@RequestBody ArtistRequest req) {
        Artist artist = artistService.addArtist(req.getName(), req.getBio());
        return ResponseEntity.ok(artist);
    }

    /** PUT /api/artists/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @RequestBody ArtistRequest req) {
        Artist updated = artistService.updateArtist(id, req.getName(), req.getBio());
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/artists/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        boolean deleted = artistService.removeArtist(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "Artist removed"));
    }
}
