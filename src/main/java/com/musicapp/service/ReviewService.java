package com.musicapp.service;

import com.musicapp.model.Review;
import com.musicapp.model.Song;
import com.musicapp.repository.ReviewRepository;
import com.musicapp.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for Review management.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SongRepository   songRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         SongRepository songRepository) {
        this.reviewRepository = reviewRepository;
        this.songRepository   = songRepository;
    }

    public Review addReview(int userId, int songId, int rating, String comment) {
        int id = reviewRepository.nextId();
        Review review = new Review(id, rating, comment, userId, songId);
        Review saved  = reviewRepository.save(review);
        songRepository.findById(songId).ifPresent(s -> s.addReview(saved));
        return saved;
    }

    public Optional<Review> findById(int id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findBySong(int songId) {
        return reviewRepository.findBySongId(songId);
    }

    public List<Review> findByUser(int userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review editReview(int reviewId, int rating, String comment) {
        return reviewRepository.findById(reviewId).map(r -> {
            r.editReview(rating, comment);
            return reviewRepository.save(r);
        }).orElse(null);
    }

    public boolean deleteReview(int reviewId) {
        return reviewRepository.deleteById(reviewId);
    }
}
