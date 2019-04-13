package com.kyan7.cinephilia.domain.models.view;

import java.util.HashMap;

public class ArticleDetailsViewModel {

    private String id;
    private String title;
    private String user;
    private long views;
    private String imageUrl;
    private HashMap<String, String> associatedMovies;
    private String content;

    public ArticleDetailsViewModel() {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public HashMap<String, String> getAssociatedMovies() {
        return associatedMovies;
    }

    public void setAssociatedMovies(HashMap<String, String> associatedMovies) {
        this.associatedMovies = associatedMovies;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
