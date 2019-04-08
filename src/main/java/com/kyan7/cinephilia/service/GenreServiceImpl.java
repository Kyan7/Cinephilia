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

    @Override
    public List<GenreServiceModel> findAllGenresOrderByName() {
        return this.genreRepository.findAllByOrderByNameAsc()
                .stream()
                .map(g -> this.modelMapper.map(g, GenreServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public GenreServiceModel addGenre(GenreServiceModel genreServiceModel) {
        Genre genre = this.modelMapper.map(genreServiceModel, Genre.class);
        return this.modelMapper.map(this.genreRepository.saveAndFlush(genre), GenreServiceModel.class);
    }

    @Override
    public void deleteGenre(String id) {
        return;
    }
}
