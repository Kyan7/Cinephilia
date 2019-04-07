package com.kyan7.cinephilia.domain.models.view;

import java.time.LocalDate;
import java.util.List;

public class MovieAdminListViewModel {

    private String id;
    private String title;
    private String creator;
    private long views;
    private List<String> genres;
    private LocalDate releaseDate;

    public MovieAdminListViewModel() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}
