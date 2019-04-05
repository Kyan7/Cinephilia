package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.domain.models.view.MovieHomeViewModel;
import com.kyan7.cinephilia.service.MovieService;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController extends BaseController {

    private final UserService userService;
    private final MovieService movieService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(UserService userService, MovieService movieService, ModelMapper modelMapper) {
        this.userService = userService;
        this.movieService = movieService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return super.view("index");
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return super.view("home");
    }
}

