package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.models.service.ReviewServiceModel;

import java.util.List;

public interface ReviewService {

    List<ReviewServiceModel> findAllByMovieId(String movieId);

    ReviewServiceModel addReview(ReviewServiceModel reviewServiceModel);
}
