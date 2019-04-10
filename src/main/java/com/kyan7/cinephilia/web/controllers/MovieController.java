package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.binding.MovieAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.MovieServiceModel;
import com.kyan7.cinephilia.domain.models.service.UserServiceModel;
import com.kyan7.cinephilia.domain.models.view.GenreViewModel;
import com.kyan7.cinephilia.domain.models.view.MovieAdminListViewModel;
import com.kyan7.cinephilia.domain.models.view.MovieDetailsViewModel;
import com.kyan7.cinephilia.domain.models.view.UserAuthoritiesViewModel;
import com.kyan7.cinephilia.service.CloudinaryService;
import com.kyan7.cinephilia.service.GenreService;
import com.kyan7.cinephilia.service.MovieService;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movies")
public class MovieController extends BaseController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;


    @Autowired
    public MovieController(MovieService movieService, GenreService genreService, UserService userService, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView allMovies(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "All Movies");
        List<MovieAdminListViewModel> movies = this.movieService.findAllMovies()
                .stream()
                .map(m -> {
                    MovieAdminListViewModel movie = this.modelMapper.map(m, MovieAdminListViewModel.class);
                    movie.setGenres(m.getGenres()
                            .stream()
                            .map(g -> g.getName())
                            .collect(Collectors.toList()));
                    movie.setUser(m.getUser().getUsername());
                    return movie;
                })
                .collect(Collectors.toList());
        modelAndView.addObject("movies", movies);
        return super.view("movie/all-movies", modelAndView);
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addMovie(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "Add Movie");
        modelAndView.addObject("genres",
                this.genreService.findAllGenresOrderByName()
                        .stream()
                        .map(g -> this.modelMapper.map(g, GenreViewModel.class))
                        .collect(Collectors.toList()));
        return super.view("movie/add-movie", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addMovieConfirm(@ModelAttribute(name = "model") MovieAddBindingModel model, Principal principal, ModelAndView modelAndView) throws IOException {
        MovieServiceModel movieServiceModel = this.modelMapper.map(model, MovieServiceModel.class);
        movieServiceModel.setGenres(
                this.genreService.findAllGenresOrderByName()
                        .stream()
                        .filter(g -> model.getGenres().contains(g.getId()))
                        .collect(Collectors.toList())
        );
        movieServiceModel.setImageUrl(
                this.cloudinaryService.uploadImage(model.getImage())
        );
        movieServiceModel.setUser(this.userService.findUserByUsername(principal.getName()));
        this.movieService.addMovie(movieServiceModel);
        return super.redirect("/movies/all");
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsMovie(@PathVariable String id, Principal principal, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(principal.getName());
        UserAuthoritiesViewModel currentUser = this.modelMapper.map(userServiceModel, UserAuthoritiesViewModel.class);
        currentUser.setAuthorities(userServiceModel.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()));
        modelAndView.addObject("currentUser", currentUser);

        MovieServiceModel movieServiceModel = this.movieService.findMovieByIdAndIncrementViews(id);
        modelAndView.addObject("pageTitle", movieServiceModel.getTitle());

        MovieDetailsViewModel movie = this.modelMapper.map(movieServiceModel, MovieDetailsViewModel.class);
        movie.setUser(movieServiceModel.getUser().getUsername());
        movie.setGenres(movieServiceModel.getGenres()
                .stream()
                .map(g -> g.getName())
                .distinct()
                .collect(Collectors.toList()));
        List<String> trailerLinks = Arrays.asList(movieServiceModel
                .getTrailerLinks()
                .split(", "));
        List<String> trailerIds = new ArrayList<>();
        for (String trailerLink : trailerLinks
             ) {
            trailerIds.add(trailerLink.split("=")[1]);
        }
        movie.setTrailerIds(trailerIds);
        modelAndView.addObject("movie", movie);
        return super.view("movie/details-movie", modelAndView);
    }
}
