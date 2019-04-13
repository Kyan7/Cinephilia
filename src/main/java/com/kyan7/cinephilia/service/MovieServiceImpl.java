package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.entities.Genre;
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
    public List<MovieServiceModel> findAllMoviesOrderByTitle() {
        return this.movieRepository.findAllByOrderByTitle()
                .stream()
                .map(m -> this.modelMapper.map(m, MovieServiceModel.class))
                .collect(Collectors.toList());
    }

    //TODO


    @Override
    public MovieServiceModel addMovie(MovieServiceModel movieServiceModel) {
        Movie movie = this.movieRepository.findByTitle(movieServiceModel.getTitle())
                .orElse(null);
        if (movie != null) {
            throw new IllegalArgumentException("Movie already exists!");
        }
        movie = this.modelMapper.map(movieServiceModel, Movie.class);
        this.movieRepository.saveAndFlush(movie);
        return this.modelMapper.map(movie, MovieServiceModel.class);
    }

    @Override
    public MovieServiceModel findMovieByIdAndIncrementViews(String id) {
        Movie movie = this.movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found!"));
        movie.setViews(movie.getViews() + 1);
        return this.modelMapper.map(this.movieRepository.saveAndFlush(movie), MovieServiceModel.class);
    }

    @Override
    public MovieServiceModel findMovieById(String id) {
        Movie movie = this.movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found!"));
        return this.modelMapper.map(movie, MovieServiceModel.class);
    }

    @Override
    public MovieServiceModel editMovie(String id, MovieServiceModel movieServiceModel, boolean isGenresEdited) {
        Movie movie = this.movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found!"));
        movie.setTitle(movieServiceModel.getTitle());
        movie.setImdbRating(movieServiceModel.getImdbRating());
        movie.setRottenTomatoesPercent(movieServiceModel.getRottenTomatoesPercent());
        movie.setBudget(movieServiceModel.getBudget());
        movie.setBoxOffice(movieServiceModel.getBoxOffice());
        if (isGenresEdited) {
            movie.setGenres(
                    movieServiceModel.getGenres()
                            .stream()
                            .map(c -> this.modelMapper.map(c, Genre.class))
                            .collect(Collectors.toList())
            );
        }
        movie.setRuntime(movieServiceModel.getRuntime());
        movie.setReleaseDate(movieServiceModel.getReleaseDate());
        movie.setCountries(movieServiceModel.getCountries());
        movie.setDirectors(movieServiceModel.getDirectors());
        movie.setLeadActor(movieServiceModel.getLeadActor());
        movie.setSupportingActors(movieServiceModel.getSupportingActors());
        movie.setDescription(movieServiceModel.getDescription());
        movie.setTrailerLinks(movieServiceModel.getTrailerLinks());

        return this.modelMapper.map(this.movieRepository.saveAndFlush(movie), MovieServiceModel.class);
    }

    @Override
    public MovieServiceModel editMovieWithEditedGenres(String id, MovieServiceModel movieServiceModel) {
        return editMovie(id, movieServiceModel, true);
    }

    @Override
    public MovieServiceModel editMovieWithUneditedGenres(String id, MovieServiceModel movieServiceModel) {
        return editMovie(id, movieServiceModel, false);
    }

    @Override
    public MovieServiceModel deleteMovie(String id) {
        Movie movie = this.movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found!"));
        this.movieRepository.delete(movie);
        return this.modelMapper.map(movie, MovieServiceModel.class);
    }

}
