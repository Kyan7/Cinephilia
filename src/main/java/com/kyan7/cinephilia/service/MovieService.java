package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.models.service.MovieServiceModel;

import java.util.List;

public interface MovieService {

    //TODO List<MovieServiceModel> findTop3Movies();

    List<MovieServiceModel> findAllMovies();

    MovieServiceModel addMovie(MovieServiceModel movieServiceModel);

    MovieServiceModel findMovieByIdAndIncrementViews(String id);

    MovieServiceModel findMovieById(String id);

    MovieServiceModel editMovie(String id, MovieServiceModel movieServiceModel, boolean isGenresEdited);

    MovieServiceModel editMovieWithEditedGenres(String id, MovieServiceModel movieServiceModel);

    MovieServiceModel editMovieWithUneditedGenres(String id, MovieServiceModel movieServiceModel);

    MovieServiceModel deleteMovie(String id);
}
