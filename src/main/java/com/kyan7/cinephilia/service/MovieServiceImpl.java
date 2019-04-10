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

    //TODO


    @Override
    public MovieServiceModel addMovie(MovieServiceModel movieServiceModel) {
        System.out.println("title: " + movieServiceModel.getTitle());
        movieServiceModel.getGenres().stream().map(g -> {
            System.out.println("genre: " + g.getName());
            return g;
        });
        System.out.println("imdb: " + movieServiceModel.getImdbRating());
        System.out.println("rt: " + movieServiceModel.getRottenTomatoesPercent());
        System.out.println("budget: " + movieServiceModel.getBudget());
        System.out.println("bo: " + movieServiceModel.getBoxOffice());
        System.out.println("runtime: " + movieServiceModel.getRuntime());
        System.out.println("relD: " + movieServiceModel.getReleaseDate().toString());
        System.out.println("countries: " + movieServiceModel.getCountries());
        System.out.println("dirs: " + movieServiceModel.getDirectors());
        System.out.println("lead: " + movieServiceModel.getLeadActor());
        System.out.println("sup: " + movieServiceModel.getSupportingActors());
        System.out.println("descr: " + movieServiceModel.getDescription());
        System.out.println("trailers: " + movieServiceModel.getTrailerLinks());
        System.out.println("image: " + movieServiceModel.getImageUrl());
        Movie movie = this.movieRepository.findByTitle(movieServiceModel.getTitle()).orElse(null);
        if (movie != null) {
            throw new IllegalArgumentException("Movie already exists!");
        }
        movie = this.modelMapper.map(movieServiceModel, Movie.class);
        System.out.println("title: " + movie.getTitle());
        movie.getGenres().stream().map(g -> {
            System.out.println("genre: " + g.getName());
            return g;
        });
        System.out.println("imdb: " + movie.getImdbRating());
        System.out.println("rt: " + movie.getRottenTomatoesPercent());
        System.out.println("budget: " + movie.getBudget());
        System.out.println("bo: " + movie.getBoxOffice());
        System.out.println("runtime: " + movie.getRuntime());
        System.out.println("relD: " + movie.getReleaseDate().toString());
        System.out.println("countries: " + movie.getCountries());
        System.out.println("dirs: " + movie.getDirectors());
        System.out.println("lead: " + movie.getLeadActor());
        System.out.println("sup: " + movie.getSupportingActors());
        System.out.println("descr: " + movie.getDescription());
        System.out.println("trailers: " + movie.getTrailerLinks());
        System.out.println("image: " + movie.getImageUrl());
        this.movieRepository.saveAndFlush(movie);
        return this.modelMapper.map(movie, MovieServiceModel.class);
    }

    @Override
    public MovieServiceModel findMovieByIdAndIncrementViews(String id) {
        Movie movie = this.movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie id not found!"));
        movie.setViews(movie.getViews() + 1);
        this.movieRepository.saveAndFlush(movie);
        return this.modelMapper.map(movie, MovieServiceModel.class);
    }

    @Override
    public MovieServiceModel findMovieById(String id) {
        Movie movie = this.movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie id not found!"));
        return this.modelMapper.map(movie, MovieServiceModel.class);
    }
}
