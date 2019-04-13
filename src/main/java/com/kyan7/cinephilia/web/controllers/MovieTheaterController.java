package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.binding.MovieTheaterAddBindingModel;
import com.kyan7.cinephilia.domain.models.service.MovieTheaterServiceModel;
import com.kyan7.cinephilia.domain.models.view.MovieTheaterAdminListViewModel;
import com.kyan7.cinephilia.domain.models.view.MovieTheaterBasicViewModel;
import com.kyan7.cinephilia.service.MovieTheaterService;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movie-theaters")
public class MovieTheaterController extends BaseController {

    private final MovieTheaterService movieTheaterService;
    private final ModelMapper modelMapper;

    @Autowired
    protected MovieTheaterController(UserService userService, ModelMapper modelMapper, MovieTheaterService movieTheaterService, ModelMapper modelMapper1) {
        super(userService, modelMapper);
        this.movieTheaterService = movieTheaterService;
        this.modelMapper = modelMapper1;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView allMovieTheaters(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", "All Movie Theaters");
        List<MovieTheaterAdminListViewModel> movieTheaters = this.movieTheaterService.findAllMovieTheaters()
                .stream()
                .map(t -> this.modelMapper.map(t, MovieTheaterAdminListViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("movieTheaters", movieTheaters);
        return view("movie-theater/all-movie-theaters", modelAndView);
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addMovieTheater(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("pageTitle", "Add Movie Theater");
            return view("movie-theater/add-movie-theater", modelAndView);
        } catch (Exception e) {
            return redirect("/movie-theaters/all");
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addMovieTheaterConfirm(@ModelAttribute(name = "model") MovieTheaterAddBindingModel model) {
        try {
            MovieTheaterServiceModel movieTheaterServiceModel = this.modelMapper.map(model, MovieTheaterServiceModel.class);
            this.movieTheaterService.addMovieTheater(movieTheaterServiceModel);
            return redirect("/movie-theaters/all");
        } catch (Exception e) {
            return redirect("/movie-theaters/all");
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editMovieTheater(@PathVariable String id, ModelAndView modelAndView) {
        try {
            MovieTheaterServiceModel movieTheaterServiceModel = this.movieTheaterService.findMovieTheaterById(id);
            MovieTheaterAddBindingModel model = this.modelMapper.map(movieTheaterServiceModel, MovieTheaterAddBindingModel.class);
            modelAndView.addObject("pageTitle", "Edit t:" + model.getName());
            modelAndView.addObject("movieTheater", model);
            modelAndView.addObject("movieTheaterId", id);

            return view("movie-theater/edit-movie-theater", modelAndView);
        } catch (Exception e) {
            return redirect("/movie-theaters/all");
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editMovieTheaterConfirm(@PathVariable String id, @ModelAttribute MovieTheaterAddBindingModel model) {
        try {
            MovieTheaterServiceModel movieTheaterServiceModel = this.modelMapper.map(model, MovieTheaterServiceModel.class);
            this.movieTheaterService.editMovieTheater(id, movieTheaterServiceModel);
            return redirect("/movie-theaters/details/" + id);
        } catch (Exception e) {
            return redirect("/movie-theaters/details/" + id);
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteMovieTheater(@PathVariable String id) {
        try {
            this.movieTheaterService.deleteMovie(id);
            return redirect("/movie-theaters/all");
        } catch (Exception e) {
            return redirect("/movie-theaters/all");
        }
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<MovieTheaterBasicViewModel> fetchMovies() {
        return this.movieTheaterService.findAllMovieTheaters()
                .stream()
                .map(t -> this.modelMapper.map(t, MovieTheaterBasicViewModel.class))
                .collect(Collectors.toList());
    }
}
