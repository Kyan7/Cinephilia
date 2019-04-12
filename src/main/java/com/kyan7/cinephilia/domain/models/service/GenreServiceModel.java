package com.kyan7.cinephilia.domain.models.service;

import java.util.List;

public class GenreServiceModel {

    private String id;
    private String name;
    private List<MovieServiceModel> movies;

    public GenreServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MovieServiceModel> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieServiceModel> movies) {
        this.movies = movies;
    }
}
