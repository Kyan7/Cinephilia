package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.entities.MovieTheater;
import com.kyan7.cinephilia.domain.models.service.MovieTheaterServiceModel;
import com.kyan7.cinephilia.repository.MovieTheaterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieTheaterServiceImpl implements MovieTheaterService {

    private final MovieTheaterRepository movieTheaterRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MovieTheaterServiceImpl(MovieTheaterRepository movieTheaterRepository, ModelMapper modelMapper) {
        this.movieTheaterRepository = movieTheaterRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<MovieTheaterServiceModel> findAllMovieTheaters() {
        return this.movieTheaterRepository.findAll()
                .stream()
                .map(t -> this.modelMapper.map(t, MovieTheaterServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public MovieTheaterServiceModel addMovieTheater(MovieTheaterServiceModel movieTheaterServiceModel) {
        MovieTheater movieTheater = this.movieTheaterRepository.findByName(movieTheaterServiceModel.getName())
                .orElse(null);
        if (movieTheater != null) {
            throw new IllegalArgumentException("Movie theater already exists!");
        }
        movieTheater = this.modelMapper.map(movieTheaterServiceModel, MovieTheater.class);
        return this.modelMapper.map(this.movieTheaterRepository.saveAndFlush(movieTheater), MovieTheaterServiceModel.class);
    }

    @Override
    public MovieTheaterServiceModel findMovieTheaterById(String id) {
        MovieTheater movieTheater = this.movieTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie theater id not found!"));
        return this.modelMapper.map(movieTheater, MovieTheaterServiceModel.class);
    }

    @Override
    public MovieTheaterServiceModel editMovieTheater(String id, MovieTheaterServiceModel movieTheaterServiceModel) {
        MovieTheater movieTheater = this.movieTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie theater id not found!"));
        movieTheater.setName(movieTheaterServiceModel.getName());
        movieTheater.setAddress(movieTheaterServiceModel.getAddress());
        movieTheater.setLink(movieTheaterServiceModel.getLink());
        movieTheater.setPhoneNumber(movieTheaterServiceModel.getPhoneNumber());
        return this.modelMapper.map(this.movieTheaterRepository.saveAndFlush(movieTheater), MovieTheaterServiceModel.class);
    }

    @Override
    public MovieTheaterServiceModel deleteMovie(String id) {
        MovieTheater movieTheater = this.movieTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie theater id not found!"));
        this.movieTheaterRepository.delete(movieTheater);
        return this.modelMapper.map(movieTheater, MovieTheaterServiceModel.class);
    }


}
