package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.models.service.GenreServiceModel;

import java.util.List;

public interface GenreService {

    List<GenreServiceModel> findAllGenresOrderByName();

    GenreServiceModel addGenre(GenreServiceModel genreServiceModel);

    GenreServiceModel findGenreById(String id);

    GenreServiceModel editGenre(String id, GenreServiceModel genreServiceModel);

    GenreServiceModel deleteGenre(String id);
}
