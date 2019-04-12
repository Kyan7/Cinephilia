package com.kyan7.cinephilia.domain.models.binding;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ArticleAddBindingModel {

    private String title;
    private String user;
    private MultipartFile image;
    private List<String> associatedMovies;
    private String content;

    public ArticleAddBindingModel() {
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public List<String> getAssociatedMovies() {
        return associatedMovies;
    }

    public void setAssociatedMovies(List<String> associatedMovies) {
        this.associatedMovies = associatedMovies;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
