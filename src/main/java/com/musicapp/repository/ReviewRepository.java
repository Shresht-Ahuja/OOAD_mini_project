package com.musicapp.repository;

import com.musicapp.model.Review;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In-memory Review store.
 */
@Repository
public class ReviewRepository {

    private final Map<Integer, Review> store      = new HashMap<>();
    private final AtomicInteger        idSequence = new AtomicInteger(10);

    public Review save(Review review) {
        if (review.getReviewId() == 0) {
            review.setReviewId(idSequence.incrementAndGet());
        }
        store.put(review.getReviewId(), review);
        return review;
    }

    public Optional<Review> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Review> findBySongId(int songId) {
        return store.values().stream()
                .filter(r -> r.getSongId() == songId && !r.isDeleted())
                .collect(Collectors.toList());
    }

    public List<Review> findByUserId(int userId) {
        return store.values().stream()
                .filter(r -> r.getUserId() == userId && !r.isDeleted())
                .collect(Collectors.toList());
    }

    public List<Review> findAll() {
        return store.values().stream()
                .filter(r -> !r.isDeleted())
                .collect(Collectors.toList());
    }

    public boolean deleteById(int id) {
        Review r = store.get(id);
        if (r != null) { r.deleteReview(); return true; }
        return false;
    }

    public int nextId() { return idSequence.incrementAndGet(); }
}
