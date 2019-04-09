package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.binding.MovieAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.MovieServiceModel;
import com.kyan7.cinephilia.domain.models.view.GenreViewModel;
import com.kyan7.cinephilia.domain.models.view.MovieAdminListViewModel;
import com.kyan7.cinephilia.service.CloudinaryService;
import com.kyan7.cinephilia.service.GenreService;
import com.kyan7.cinephilia.service.MovieService;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
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

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView moviesList() {
        return null;
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
                    System.out.println();
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
        System.out.println("Controller: " + movieServiceModel.getTitle());
        this.movieService.addMovie(movieServiceModel);
        return super.redirect("/movies/all");
    }
}
