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

    /**
     * Finds all movies in the database.
     * @return a list of movie service models.
     */
    @Override
    public List<MovieServiceModel> findAllMovies() {
        return this.movieRepository.findAll()
                .stream()
                .map(m -> this.modelMapper.map(m, MovieServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Finds all movies in the database and orders them by title.
     * @return a list of movie service models (sorted by name).
     */
    @Override
    public List<MovieServiceModel> findAllMoviesOrderByTitle() {
        return this.movieRepository.findAllByOrderByTitle()
                .stream()
                .map(m -> this.modelMapper.map(m, MovieServiceModel.class))
                .collect(Collectors.toList());
    }


    /**
     * Attempts to add a movie to the database.
     * @param movieServiceModel transfers the movie's data to the method.
     * @return a respective model of the movie.
     */
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

    /**
     * Attempts to find a movie and increase its views by 1.
     * @param id is the id of the movie we're searching for.
     * @return a respective model of the movie.
     */
    @Override
    public MovieServiceModel findMovieByIdAndIncrementViews(String id) {
        Movie movie = this.movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found!"));
        movie.setViews(movie.getViews() + 1);
        return this.modelMapper.map(this.movieRepository.saveAndFlush(movie), MovieServiceModel.class);
    }

    /**
     * Attempts to find a movie.
     * @param id is the id of the movie we're searching for.
     * @return a respective model of the movie.
     */
    @Override
    public MovieServiceModel findMovieById(String id) {
        Movie movie = this.movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found!"));
        return this.modelMapper.map(movie, MovieServiceModel.class);
    }

    /**
     * Attempts to edit a movie. Depending on whether it's had its genres edited the method chooses whether to set new ones.
     * @param id is the id of the movie we're editing.
     * @param movieServiceModel transfers the movie's new data to the method.
     * @param isGenresEdited shows us whether there the movie's genres have been changed.
     * @return a respective model of the movie.
     */
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

    /**
     * Attempts to edit a movie which has had its genres changed.
     * @param id is the id of the movie we're editing.
     * @param movieServiceModel transfers the movie's new data to the method.
     * @return a respective model of the movie.
     */
    @Override
    public MovieServiceModel editMovieWithEditedGenres(String id, MovieServiceModel movieServiceModel) {
        return editMovie(id, movieServiceModel, true);
    }

    /**
     * Attempts to edit a movie which hasn't had its genres changed.
     * @param id is the id of the movie we're editing.
     * @param movieServiceModel transfers the movie's new data to the method.
     * @return a respective model of the movie.
     */
    @Override
    public MovieServiceModel editMovieWithUneditedGenres(String id, MovieServiceModel movieServiceModel) {
        return editMovie(id, movieServiceModel, false);
    }

    /**
     * Attempts to delete a movie.
     * @param id is the id of the movie we are deleting.
     * @return a respective model of the movie.
     */
    @Override
    public MovieServiceModel deleteMovie(String id) {
        Movie movie = this.movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found!"));
        this.movieRepository.delete(movie);
        return this.modelMapper.map(movie, MovieServiceModel.class);
    }

}
