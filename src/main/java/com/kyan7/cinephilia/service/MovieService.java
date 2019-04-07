package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.models.service.MovieServiceModel;

import java.util.List;

public interface MovieService {

    //TODO List<MovieServiceModel> findTop3Movies();

    List<MovieServiceModel> findAllMovies();

    MovieServiceModel addMovie(MovieServiceModel movieServiceModel);
}
