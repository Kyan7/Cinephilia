package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.view.MovieHomeViewModel;
import com.kyan7.cinephilia.service.MovieService;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController {


    private final UserService userService;
    private final MovieService movieService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(UserService userService, MovieService movieService, ModelMapper modelMapper) {
        super(userService, modelMapper);
        this.userService = userService;
        this.movieService = movieService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView index(Principal principal, ModelAndView modelAndView) {
        try {
            principal.getName();
            return redirect("/home");
        } catch (Exception e) {
            modelAndView.addObject("pageTitle", "Index");
            return view("index", modelAndView);
        }
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView home(Principal principal, ModelAndView modelAndView) {
        modelAndView.addObject("currentUser", principal.getName());
        List<MovieHomeViewModel> movies = this.movieService
                .findAllMovies()
                .stream()
                .map(m -> this.modelMapper.map(m, MovieHomeViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("movies", movies);
        modelAndView.addObject("pageTitle", "Home");
        return view("home", modelAndView);
    }
}

