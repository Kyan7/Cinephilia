package com.kyan7.cinephilia.domain.models.service;

public class MovieEditServiceModel {

    private String id;
    private String editorComments;
    private String timeStamp;
    private MovieServiceModel movie;
    private UserServiceModel user;
    private String editedTitle;
    private String editedImageLink;
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

    public MovieEditServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEditorComments() {
        return editorComments;
    }

    public void setEditorComments(String editorComments) {
        this.editorComments = editorComments;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public MovieServiceModel getMovie() {
        return movie;
    }

    public void setMovie(MovieServiceModel movie) {
        this.movie = movie;
    }

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    public String getEditedTitle() {
        return editedTitle;
    }

    public void setEditedTitle(String editedTitle) {
        this.editedTitle = editedTitle;
    }

    public String getEditedImageLink() {
        return editedImageLink;
    }

    public void setEditedImageLink(String editedImageLink) {
        this.editedImageLink = editedImageLink;
    }

    public String getEditedImdbRating() {
        return editedImdbRating;
    }

    public void setEditedImdbRating(String editedImdbRating) {
        this.editedImdbRating = editedImdbRating;
    }

    public String getEditedRottenTomatoesPercent() {
        return editedRottenTomatoesPercent;
    }

    public void setEditedRottenTomatoesPercent(String editedRottenTomatoesPercent) {
        this.editedRottenTomatoesPercent = editedRottenTomatoesPercent;
    }

    public String getEditedBudget() {
        return editedBudget;
    }

    public void setEditedBudget(String editedBudget) {
        this.editedBudget = editedBudget;
    }

    public String getEditedBoxOffice() {
        return editedBoxOffice;
    }

    public void setEditedBoxOffice(String editedBoxOffice) {
        this.editedBoxOffice = editedBoxOffice;
    }

    public String getEditedGenres() {
        return editedGenres;
    }

    public void setEditedGenres(String editedGenres) {
        this.editedGenres = editedGenres;
    }

    public String getEditedRuntime() {
        return editedRuntime;
    }

    public void setEditedRuntime(String editedRuntime) {
        this.editedRuntime = editedRuntime;
    }

    public String getEditedReleaseDate() {
        return editedReleaseDate;
    }

    public void setEditedReleaseDate(String editedReleaseDate) {
        this.editedReleaseDate = editedReleaseDate;
    }

    public String getEditedCountries() {
        return editedCountries;
    }

    public void setEditedCountries(String editedCountries) {
        this.editedCountries = editedCountries;
    }

    public String getEditedDirectors() {
        return editedDirectors;
    }

    public void setEditedDirectors(String editedDirectors) {
        this.editedDirectors = editedDirectors;
    }

    public String getEditedStudios() {
        return editedStudios;
    }

    public void setEditedStudios(String editedStudios) {
        this.editedStudios = editedStudios;
    }

    public String getEditedLeadActor() {
        return editedLeadActor;
    }

    public void setEditedLeadActor(String editedLeadActor) {
        this.editedLeadActor = editedLeadActor;
    }

    public String getEditedSupportingActors() {
        return editedSupportingActors;
    }

    public void setEditedSupportingActors(String editedSupportingActors) {
        this.editedSupportingActors = editedSupportingActors;
    }

    public String getEditedDescription() {
        return editedDescription;
    }

    public void setEditedDescription(String editedDescription) {
        this.editedDescription = editedDescription;
    }

    public String getEditedTrailerLinks() {
        return editedTrailerLinks;
    }

    public void setEditedTrailerLinks(String editedTrailerLinks) {
        this.editedTrailerLinks = editedTrailerLinks;
    }

    public boolean isScreeningsEdited() {
        return isScreeningsEdited;
    }

    public void setScreeningsEdited(boolean screeningsEdited) {
        isScreeningsEdited = screeningsEdited;
    }
}
