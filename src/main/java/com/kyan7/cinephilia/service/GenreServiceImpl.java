package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.entities.Genre;
import com.kyan7.cinephilia.domain.models.service.GenreServiceModel;
import com.kyan7.cinephilia.repository.GenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final MovieService movieService;
    private final ModelMapper modelMapper;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, MovieService movieService, ModelMapper modelMapper) {
        this.genreRepository = genreRepository;
        this.movieService = movieService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<GenreServiceModel> findAllGenresOrderByName() {
        return this.genreRepository.findAllByOrderByNameAsc()
                .stream()
                .map(g -> this.modelMapper.map(g, GenreServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public GenreServiceModel addGenre(GenreServiceModel genreServiceModel) {
        Genre genre = this.genreRepository.findByName(genreServiceModel.getName())
                .orElse(null);
        if (genre != null) {
            throw new IllegalArgumentException("Genre with that name already exists");
        }
        genre = this.modelMapper.map(genreServiceModel, Genre.class);
        return this.modelMapper.map(this.genreRepository.saveAndFlush(genre), GenreServiceModel.class);
    }

    @Override
    public GenreServiceModel findGenreById(String id) {
        Genre genre = this.genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found!"));
        return this.modelMapper.map(genre, GenreServiceModel.class);
    }

    @Override
    public GenreServiceModel editGenre(String id, GenreServiceModel genreServiceModel) {
        Genre genre = this.genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found!"));
        genre.setName(genreServiceModel.getName());
        return this.modelMapper.map(this.genreRepository.saveAndFlush(genre), GenreServiceModel.class);
    }

    @Override
    public GenreServiceModel deleteGenre(String id) {
        Genre genre = this.genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found!"));
        this.genreRepository.delete(genre);
        return this.modelMapper.map(genre, GenreServiceModel.class);
    }
}
