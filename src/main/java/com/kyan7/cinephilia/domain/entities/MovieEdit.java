package com.kyan7.cinephilia.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "movie_edits")
public class MovieEdit extends Edit {

    private Movie movie;
    private User user;
    private String editedTitle;
    private String editedImdbRating;
    private String editedRottenTomatoesPercent;
    private String editedBudget;
    private String editedBoxOffice;
    private String editedGenres;
    private String editedRuntime;
    private String editedReleaseDate;
    private String editedCountries;
    private String editedDirectors;
    private String editedStudios;
    private String editedLeadActor;
    private String editedSupportingActors;
    private String editedDescription;
    private String editedTrailerLinks;
    private boolean isScreeningsEdited;

    public MovieEdit() {
    }

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "edited_title", updatable = false)
    public String getEditedTitle() {
        return editedTitle;
    }

    public void setEditedTitle(String editedTitle) {
        this.editedTitle = editedTitle;
    }

    @Column(name = "edited_imdb_rating", updatable = false)
    public String getEditedImdbRating() {
        return editedImdbRating;
    }

    public void setEditedImdbRating(String editedImdbRating) {
        this.editedImdbRating = editedImdbRating;
    }

    @Column(name = "edited_rotten_tomatoes_percent", updatable = false)
    public String getEditedRottenTomatoesPercent() {
        return editedRottenTomatoesPercent;
    }

    public void setEditedRottenTomatoesPercent(String editedRottenTomatoesPercent) {
        this.editedRottenTomatoesPercent = editedRottenTomatoesPercent;
    }

    @Column(name = "edited_budget", updatable = false)
    public String getEditedBudget() {
        return editedBudget;
    }

    public void setEditedBudget(String editedBudget) {
        this.editedBudget = editedBudget;
    }

    @Column(name = "edited_box_office", updatable = false)
    public String getEditedBoxOffice() {
        return editedBoxOffice;
    }

    public void setEditedBoxOffice(String editedBoxOffice) {
        this.editedBoxOffice = editedBoxOffice;
    }

    @Column(name = "edited_genres", updatable = false)
    public String getEditedGenres() {
        return editedGenres;
    }

    public void setEditedGenres(String editedGenres) {
        this.editedGenres = editedGenres;
    }

    @Column(name = "edited_runtime", updatable = false)
    public String getEditedRuntime() {
        return editedRuntime;
    }

    public void setEditedRuntime(String editedRuntime) {
        this.editedRuntime = editedRuntime;
    }

    @Column(name = "edited_release_date", updatable = false)
    public String getEditedReleaseDate() {
        return editedReleaseDate;
    }

    public void setEditedReleaseDate(String editedReleaseDate) {
        this.editedReleaseDate = editedReleaseDate;
    }

    @Column(name = "edited_countries", updatable = false)
    public String getEditedCountries() {
        return editedCountries;
    }

    public void setEditedCountries(String editedCountries) {
        this.editedCountries = editedCountries;
    }

    @Column(name = "edited_directors", updatable = false)
    public String getEditedDirectors() {
        return editedDirectors;
    }

    public void setEditedDirectors(String editedDirectors) {
        this.editedDirectors = editedDirectors;
    }

    @Column(name = "edited_studios", updatable = false)
    public String getEditedStudios() {
        return editedStudios;
    }

    public void setEditedStudios(String editedStudios) {
        this.editedStudios = editedStudios;
    }

    @Column(name = "edited_lead_actor", updatable = false)
    public String getEditedLeadActor() {
        return editedLeadActor;
    }

    public void setEditedLeadActor(String editedLeadActor) {
        this.editedLeadActor = editedLeadActor;
    }

    @Column(name = "edited_supporting_actors", updatable = false)
    public String getEditedSupportingActors() {
        return editedSupportingActors;
    }

    public void setEditedSupportingActors(String editedSupportingActors) {
        this.editedSupportingActors = editedSupportingActors;
    }

    @Column(name = "edited_description", columnDefinition = "TEXT", updatable = false)
    public String getEditedDescription() {
        return editedDescription;
    }

    public void setEditedDescription(String editedDescription) {
        this.editedDescription = editedDescription;
    }

    @Column(name = "edited_trailer_links", updatable = false)
    public String getEditedTrailerLinks() {
        return editedTrailerLinks;
    }

    public void setEditedTrailerLinks(String editedTrailerLinks) {
        this.editedTrailerLinks = editedTrailerLinks;
    }

    @Column(name = "is_screenings_edited", updatable = false)
    public boolean isScreeningsEdited() {
        return isScreeningsEdited;
    }

    public void setScreeningsEdited(boolean screeningsEdited) {
        isScreeningsEdited = screeningsEdited;
    }
}
