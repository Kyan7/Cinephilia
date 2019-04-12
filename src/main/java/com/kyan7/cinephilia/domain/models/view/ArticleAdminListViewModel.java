package com.kyan7.cinephilia.domain.models.view;

import java.util.List;

public class ArticleAdminListViewModel {

    private String id;
    private String title;
    private String user;
    private long views;
    private List<String> associatedMovies;

    public ArticleAdminListViewModel() {
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public List<String> getAssociatedMovies() {
        return associatedMovies;
    }

    public void setAssociatedMovies(List<String> associatedMovies) {
        this.associatedMovies = associatedMovies;
    }
}
