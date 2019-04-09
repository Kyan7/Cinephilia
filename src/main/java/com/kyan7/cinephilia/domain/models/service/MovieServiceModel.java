package com.kyan7.cinephilia.domain.models.service;

import java.time.LocalDate;
import java.util.List;

public class MovieServiceModel {

    private String id;
    private String title;
    private UserServiceModel user;
    private long views;
    private String imageUrl;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public long getRottenTomatoesPercent() {
        return rottenTomatoesPercent;
    }

    public void setRottenTomatoesPercent(long rottenTomatoesPercent) {
        this.rottenTomatoesPercent = rottenTomatoesPercent;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public long getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(long boxOffice) {
        this.boxOffice = boxOffice;
    }

    public List<GenreServiceModel> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreServiceModel> genres) {
        this.genres = genres;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getStudios() {
        return studios;
    }

    public void setStudios(String studios) {
        this.studios = studios;
    }

    public String getLeadActor() {
        return leadActor;
    }

    public void setLeadActor(String leadActor) {
        this.leadActor = leadActor;
    }

    public String getSupportingActors() {
        return supportingActors;
    }

    public void setSupportingActors(String supportingActors) {
        this.supportingActors = supportingActors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrailerLinks() {
        return trailerLinks;
    }

    public void setTrailerLinks(String trailerLinks) {
        this.trailerLinks = trailerLinks;
    }

    public List<ScreeningServiceModel> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<ScreeningServiceModel> screenings) {
        this.screenings = screenings;
    }

    public List<ReviewServiceModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewServiceModel> reviews) {
        this.reviews = reviews;
    }
}
