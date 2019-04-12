package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.service.MovieTheaterService;
import com.kyan7.cinephilia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie-theaters")
public class MovieTheaterController extends BaseController {

    private final MovieTheaterService movieTheaterService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    protected MovieTheaterController(UserService userService, ModelMapper modelMapper, MovieTheaterService movieTheaterService, UserService userService1, ModelMapper modelMapper1) {
        super(userService, modelMapper);
        this.movieTheaterService = movieTheaterService;
        this.userService = userService1;
        this.modelMapper = modelMapper1;
    }






}
