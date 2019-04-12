package com.kyan7.cinephilia.domain.models.service;

import java.util.List;

public class ArticleServiceModel {

    private String id;
    private String title;
    private UserServiceModel user;
    private long views;
    private String imageUrl;
    private String content;
    private List<MovieServiceModel> associatedMovies;

    public ArticleServiceModel() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MovieServiceModel> getAssociatedMovies() {
        return associatedMovies;
    }

    public void setAssociatedMovies(List<MovieServiceModel> associatedMovies) {
        this.associatedMovies = associatedMovies;
    }
}
