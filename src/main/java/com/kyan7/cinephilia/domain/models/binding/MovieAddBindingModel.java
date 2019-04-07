package com.kyan7.cinephilia.domain.models.binding;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class MovieAddBindingModel {

    private String title;
    private String creator;
    private String imageLink;
    private Double imdbRating;
    private long rottenTomatoesPercent;
    private long budget;
    private long boxOffice;
    private List<String> genres;
    private long runtime;
    private LocalDate releaseDate;
    private String countries;
    private String directors;
    private String studios;
    private String leadActor;
    private String supportingActors;
    private String description;
    private String trailerLinks;

    public MovieAddBindingModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
}
