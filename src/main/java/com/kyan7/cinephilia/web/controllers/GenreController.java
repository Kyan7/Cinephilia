package com.kyan7.cinephilia.web.controllers;

import com.kyan7.cinephilia.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;


public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }
}
