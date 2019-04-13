package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.entities.Movie;
import com.kyan7.cinephilia.domain.entities.Review;
import com.kyan7.cinephilia.domain.models.service.ReviewServiceModel;
import com.kyan7.cinephilia.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final MovieService movieService; //TODO
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, MovieService movieService, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.movieService = movieService;
        this.modelMapper = modelMapper;
    }

    /**
     * Finds all reviews to a movie.
     * @param movieId is the id of the movie.
     * @return a list of review service models.
     */
    @Override
    public List<ReviewServiceModel> findAllReviewsByMovieId(String movieId) {
        Movie movie = this.modelMapper.map(this.movieService.findMovieById(movieId), Movie.class);
        return this.reviewRepository.findAllByMovie(movie)
                .stream()
                .map(r -> this.modelMapper.map(r, ReviewServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Attempts to find a review.
     * @param id is the id of the review we are searching for.
     * @return a respective model of the review.
     */
    @Override
    public ReviewServiceModel findReviewById(String id) {
        Review review = this.reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found!"));
        return this.modelMapper.map(review, ReviewServiceModel.class);
    }

    /**
     * Adds a review.
     * @param reviewServiceModel transfers the data of the review which we are adding.
     * @return a respective model of the review.
     */
    @Override
    public ReviewServiceModel addReview(ReviewServiceModel reviewServiceModel) {
        Review review = this.modelMapper.map(reviewServiceModel, Review.class);
        return this.modelMapper.map(this.reviewRepository.saveAndFlush(review), ReviewServiceModel.class);
    }

    /**
     * Attempts to delete a review.
     * @param id is the id of the review we are deleting.
     * @return a respective model of the review.
     */
    @Override
    public ReviewServiceModel deleteReview(String id) {
        Review review = this.reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found!"));
        this.reviewRepository.delete(review);
        return this.modelMapper.map(review, ReviewServiceModel.class);
    }
}
