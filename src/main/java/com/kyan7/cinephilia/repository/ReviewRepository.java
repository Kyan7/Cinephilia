package com.kyan7.cinephilia.repository;

import com.kyan7.cinephilia.domain.entities.Movie;
import com.kyan7.cinephilia.domain.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    List<Review> findAllByMovie(Movie movie);
}
