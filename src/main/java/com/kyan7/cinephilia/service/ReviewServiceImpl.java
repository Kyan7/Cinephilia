package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.entities.Movie;
import com.kyan7.cinephilia.domain.entities.Review;
import com.kyan7.cinephilia.domain.models.service.ReviewServiceModel;
import com.kyan7.cinephilia.repository.MovieRepository;
import com.kyan7.cinephilia.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, MovieRepository movieRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<ReviewServiceModel> findAllByMovieId(String movieId) {
        Movie movie = this.movieRepository.findById(movieId).orElseThrow(() -> new IllegalArgumentException("Movie not found!"));
        return this.reviewRepository.findAllByMovie(movie)
                .stream()
                .map(r -> this.modelMapper.map(r, ReviewServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewServiceModel addReview(ReviewServiceModel reviewServiceModel) {
        Review review = this.modelMapper.map(reviewServiceModel, Review.class);
        this.reviewRepository.saveAndFlush(review);
        return this.modelMapper.map(review, ReviewServiceModel.class);
    }
}
