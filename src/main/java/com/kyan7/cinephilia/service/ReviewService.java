package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.models.service.ReviewServiceModel;

import java.util.List;

public interface ReviewService {

    List<ReviewServiceModel> findAllReviewsByMovieId(String movieId);

    ReviewServiceModel findReviewById(String id);

    ReviewServiceModel addReview(ReviewServiceModel reviewServiceModel);

    ReviewServiceModel deleteReview(String id);
}
