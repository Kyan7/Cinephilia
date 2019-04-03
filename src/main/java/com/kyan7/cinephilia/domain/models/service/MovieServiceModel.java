package com.kyan7.cinephilia.domain.models.service;

import java.time.LocalDate;
import java.util.List;

public class MovieServiceModel {


    private String id;
    private String title;
    private UserServiceModel creator;
    private List<UserServiceModel> editors;
    private long views;
    private Double imdbRating;
    private long rottenTomatoesPercent;
    private long budget;
    private long boxOffice;
    private List<GenreServiceModel> genres;
    private long runtime;
    private LocalDate releaseDate;
    private String countries;
    private String directors;
    private String studios;
    private String leadActor;
    private String supportingActors;
    private String description;
    private String trailerLinks;
    private List<ScreeningServiceModel> screenings;
    private List<ReviewServiceModel> reviews;
}
