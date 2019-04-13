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

    /**
     * Finds all movie theaters.
     * @return a list of movie theater service models.
     */
    @Override
    public List<MovieTheaterServiceModel> findAllMovieTheaters() {
        return this.movieTheaterRepository.findAll()
                .stream()
                .map(t -> this.modelMapper.map(t, MovieTheaterServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Finds all movie theaters and orders them by name.
     * @return a list of movie theater service models (sorted by name).
     */
    @Override
    public List<MovieTheaterServiceModel> findAllMovieTheatersOrderByName() {
        return this.movieTheaterRepository.findAllByOrderByNameAsc()
                .stream()
                .map(t -> this.modelMapper.map(t, MovieTheaterServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Attempts to add a movie theater.
     * @param movieTheaterServiceModel transfers the data of the movie theater to the method.
     * @return a respective model of the movie theater.
     */
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

    /**
     * Attempts to find a movie theater.
     * @param id is the id of the movie theater we are searching for.
     * @return a respective model of the movie theater.
     */
    @Override
    public MovieTheaterServiceModel findMovieTheaterById(String id) {
        MovieTheater movieTheater = this.movieTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie theater not found!"));
        return this.modelMapper.map(movieTheater, MovieTheaterServiceModel.class);
    }

    /**
     * Attempts to edit a movie theater.
     * @param id is the id of the movie theater we are editing.
     * @param movieTheaterServiceModel transfers the new data to the method.
     * @return a respective model of the movie theater.
     */
    @Override
    public MovieTheaterServiceModel editMovieTheater(String id, MovieTheaterServiceModel movieTheaterServiceModel) {
        MovieTheater movieTheater = this.movieTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie theater not found!"));
        movieTheater.setName(movieTheaterServiceModel.getName());
        movieTheater.setAddress(movieTheaterServiceModel.getAddress());
        movieTheater.setLink(movieTheaterServiceModel.getLink());
        movieTheater.setPhoneNumber(movieTheaterServiceModel.getPhoneNumber());
        return this.modelMapper.map(this.movieTheaterRepository.saveAndFlush(movieTheater), MovieTheaterServiceModel.class);
    }

    /**
     * Attempts to delete a movie theater.
     * @param id is the id of the movie we are deleting.
     * @return a respective model of the movie theater.
     */
    @Override
    public MovieTheaterServiceModel deleteMovie(String id) {
        MovieTheater movieTheater = this.movieTheaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie theater not found!"));
        this.movieTheaterRepository.delete(movieTheater);
        return this.modelMapper.map(movieTheater, MovieTheaterServiceModel.class);
    }


}
