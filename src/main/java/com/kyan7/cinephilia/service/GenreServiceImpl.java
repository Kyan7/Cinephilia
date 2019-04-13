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
    private final ModelMapper modelMapper;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, ModelMapper modelMapper) {
        this.genreRepository = genreRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Finds all genres in the database and orders them by name.
     * @return a list of genre service models (sorted by name).
     */
    @Override
    public List<GenreServiceModel> findAllGenresOrderByName() {
        return this.genreRepository.findAllByOrderByNameAsc()
                .stream()
                .map(g -> this.modelMapper.map(g, GenreServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Attempts to add a genre to the database.
     * @param genreServiceModel transfers the genre's data to the method.
     * @return a respective model of the genre.
     */
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

    /**
     * Attempts to find a genre.
     * @param id is the id of the genre we are searching for.
     * @return a respective model of the genre.
     */
    @Override
    public GenreServiceModel findGenreById(String id) {
        Genre genre = this.genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found!"));
        return this.modelMapper.map(genre, GenreServiceModel.class);
    }

    /**
     * Attempts to edit a genre.
     * @param id is the id of the genre we're editing.
     * @param genreServiceModel transfer's the genre's new data to the method.
     * @return a respective model of the genre.
     */
    @Override
    public GenreServiceModel editGenre(String id, GenreServiceModel genreServiceModel) {
        Genre genre = this.genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found!"));
        genre.setName(genreServiceModel.getName());
        return this.modelMapper.map(this.genreRepository.saveAndFlush(genre), GenreServiceModel.class);
    }

    /**
     * Attempts to delete a genre.
     * @param id is the id of the genre we're deleting.
     * @return a respective model of the genre.
     */
    @Override
    public GenreServiceModel deleteGenre(String id) {
        Genre genre = this.genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found!"));
        this.genreRepository.delete(genre);
        return this.modelMapper.map(genre, GenreServiceModel.class);
    }
}
