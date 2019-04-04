package com.kyan7.cinephilia.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "article_edits")
public class ArticleEdit extends Edit {

    private Article article;
    private User user;
    private String editedTitle;
    private String editedContent;
    private String editedAssociatedMovies;
    private String editedAssociatedArticles;

    public ArticleEdit() {
    }

    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
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

    @Column(name = "edited_content", updatable = false)
    public String getEditedContent() {
        return editedContent;
    }

    public void setEditedContent(String editedContent) {
        this.editedContent = editedContent;
    }

    @Column(name = "edited_associated_movies", updatable = false)
    public String getEditedAssociatedMovies() {
        return editedAssociatedMovies;
    }

    public void setEditedAssociatedMovies(String editedAssociatedMovies) {
        this.editedAssociatedMovies = editedAssociatedMovies;
    }

    @Column(name = "edited_associated_articles", updatable = false)
    public String getEditedAssociatedArticles() {
        return editedAssociatedArticles;
    }

    public void setEditedAssociatedArticles(String editedAssociatedArticles) {
        this.editedAssociatedArticles = editedAssociatedArticles;
    }
}
