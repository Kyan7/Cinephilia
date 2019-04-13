package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.models.service.MovieTheaterServiceModel;

import java.util.List;

public interface MovieTheaterService {

    List<MovieTheaterServiceModel> findAllMovieTheaters();

    List<MovieTheaterServiceModel> findAllMovieTheatersOrderByName();

    MovieTheaterServiceModel addMovieTheater(MovieTheaterServiceModel movieTheaterServiceModel);

    MovieTheaterServiceModel findMovieTheaterById(String id);

    MovieTheaterServiceModel editMovieTheater(String id, MovieTheaterServiceModel movieTheaterServiceModel);

    MovieTheaterServiceModel deleteMovie(String id);
}
