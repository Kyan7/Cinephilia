package com.kyan7.cinephilia.domain.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "articles")
public class Article {

    private String id;
    private String title;
    private User creator;
    private long views;
    private String content;
    private List<Movie> associatedMovies;
    private List<Article> associatedArticles;
    private List<ArticleEdit> articleEdits;

    public Article() {
    }

    @Id
    @GeneratedValue(generator = "uuid-string")
    @GenericGenerator(
            name = "uuid-string",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false, unique = true)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(name = "views")
    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToMany(targetEntity = Movie.class)
    @JoinTable(
            name = "articles_movies",
            joinColumns = @JoinColumn(name = "associated_movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id")
    )
    public List<Movie> getAssociatedMovies() {
        return associatedMovies;
    }

    public void setAssociatedMovies(List<Movie> associatedMovies) {
        this.associatedMovies = associatedMovies;
    }

    @ManyToMany(targetEntity = Article.class)
    @JoinTable(
            name = "articles_articles",
            joinColumns = @JoinColumn(name = "associated_article_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id")
    )
    public List<Article> getAssociatedArticles() {
        return associatedArticles;
    }

    public void setAssociatedArticles(List<Article> associatedArticles) {
        this.associatedArticles = associatedArticles;
    }

    @OneToMany(mappedBy = "article_edit", targetEntity = ArticleEdit.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<ArticleEdit> getArticleEdits() {
        return articleEdits;
    }

    public void setArticleEdits(List<ArticleEdit> articleEdits) {
        this.articleEdits = articleEdits;
    }
}
