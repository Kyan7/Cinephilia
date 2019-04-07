package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.entities.Movie;
import com.kyan7.cinephilia.domain.models.service.MovieServiceModel;
import com.kyan7.cinephilia.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, ModelMapper modelMapper) {
        this.movieRepository = movieRepository;
        this.modelMapper = modelMapper;
    }

    //@Override
    //public List<MovieServiceModel> findTop3Movies() {
    //    return this.movieRepository.findAllByOrderByViews()
    //            .stream()
    //            .map(m -> this.modelMapper.map(m, MovieServiceModel.class))
    //            .collect(Collectors.toList());
    //}

    @Override
    public List<MovieServiceModel> findAllMovies() {
        return this.movieRepository.findAll()
                .stream()
                .map(m -> this.modelMapper.map(m, MovieServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public MovieServiceModel addMovie(MovieServiceModel movieServiceModel) {
        Movie movie = this.modelMapper.map(movieServiceModel, Movie.class);
        return this.modelMapper.map(this.movieRepository.saveAndFlush(movie), MovieServiceModel.class);
    }
}
