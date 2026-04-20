package com.musicapp.controller;

import com.musicapp.dto.ReviewRequest;
import com.musicapp.model.Review;
import com.musicapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Review operations.
 * Base path: /api/reviews
 */
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /** GET /api/reviews */
    @GetMapping
    public ResponseEntity<List<Review>> listAll() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    /** GET /api/reviews/song/{songId} */
    @GetMapping("/song/{songId}")
    public ResponseEntity<List<Review>> getBySong(@PathVariable int songId) {
        return ResponseEntity.ok(reviewService.findBySong(songId));
    }

    /** GET /api/reviews/user/{userId} */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getByUser(@PathVariable int userId) {
        return ResponseEntity.ok(reviewService.findByUser(userId));
    }

    /** POST /api/reviews */
    @PostMapping
    public ResponseEntity<Review> create(@RequestBody ReviewRequest req) {
        Review review = reviewService.addReview(req.getUserId(), req.getSongId(),
                                                req.getRating(), req.getComment());
        return ResponseEntity.ok(review);
    }

    /** PUT /api/reviews/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable int id,
                                  @RequestBody ReviewRequest req) {
        Review updated = reviewService.editReview(id, req.getRating(), req.getComment());
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/reviews/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        boolean deleted = reviewService.deleteReview(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "Review deleted"));
    }
}
