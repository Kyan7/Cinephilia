package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.binding.MovieAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.MovieServiceModel;
import com.kyan7.cinephilia.domain.models.view.GenreListViewModel;
import com.kyan7.cinephilia.domain.models.view.MovieAdminListViewModel;
import com.kyan7.cinephilia.service.GenreService;
import com.kyan7.cinephilia.service.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movies")
public class MovieController extends BaseController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final ModelMapper modelMapper;


    @Autowired
    public MovieController(MovieService movieService, GenreService genreService, ModelMapper modelMapper) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView allMovies() {
        return null;
    }

    @GetMapping("/all/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView allMoviesAdmin(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "Movie List");
        List<MovieAdminListViewModel> movies = this.movieService.findAllMovies()
                .stream()
                .map(m -> {
                    MovieAdminListViewModel movie = this.modelMapper.map(m, MovieAdminListViewModel.class);
                    movie.setGenres(m.getGenres()
                            .stream()
                            .map(g -> g.getName())
                            .collect(Collectors.toList()));
                    return movie;
                })
                .collect(Collectors.toList());
        modelAndView.addObject("movies", movies);
        return super.view("all-movies-admin", modelAndView);
    }

    @GetMapping("/add/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView add(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "Add Movie");
        modelAndView.addObject("genres",
                this.genreService.findAllGenresOrderByName()
                        .stream()
                        .map(g -> this.modelMapper.map(g, GenreListViewModel.class))
                        .collect(Collectors.toList()));
        return super.view("add-movie-admin", modelAndView);
    }

    @PostMapping("/add/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addConfirm(@ModelAttribute(name = "model") MovieAddBindingModel model, ModelAndView modelAndView) {
        this.movieService.addMovie(this.modelMapper.map(model, MovieServiceModel.class));
        return super.redirect("/movies/all/admin");
    }
}
