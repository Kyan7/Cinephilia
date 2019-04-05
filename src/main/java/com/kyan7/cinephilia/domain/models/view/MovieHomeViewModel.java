package com.kyan7.cinephilia.domain.models.view;

public class MovieHomeViewModel {

    private String id;
    private String title;
    private long views;
    private Double imdbRating;
    private long rottenTomatoesPercent;

    public MovieHomeViewModel() {
    }

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

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
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
}
