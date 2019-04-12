package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.entities.Article;
import com.kyan7.cinephilia.domain.entities.Movie;
import com.kyan7.cinephilia.domain.models.service.ArticleServiceModel;
import com.kyan7.cinephilia.repository.ArticleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ArticleServiceModel> findAllArticles() {
        return this.articleRepository.findAll()
                .stream()
                .map(a -> this.modelMapper.map(a, ArticleServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ArticleServiceModel addArticle(ArticleServiceModel articleServiceModel) {
        Article article = this.articleRepository.findByTitle(articleServiceModel.getTitle()).orElse(null);
        if (article != null) {
            throw new IllegalArgumentException("Article already exists!");
        }
        article = this.modelMapper.map(articleServiceModel, Article.class);
        this.articleRepository.saveAndFlush(article);
        return this.modelMapper.map(article, ArticleServiceModel.class);
    }

    @Override
    public ArticleServiceModel findArticleByIdAndIncrementViews(String id) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article id not found!"));
        article.setViews(article.getViews() + 1);
        this.articleRepository.saveAndFlush(article);
        return this.modelMapper.map(article, ArticleServiceModel.class);
    }

    @Override
    public ArticleServiceModel findArticleById(String id) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article id not found!"));
        return this.modelMapper.map(article, ArticleServiceModel.class);
    }

    @Override
    public ArticleServiceModel editArticle(String id, ArticleServiceModel articleServiceModel, boolean isAssociatedMoviesEdited) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article id not found!"));
        article.setTitle(articleServiceModel.getTitle());
        if (isAssociatedMoviesEdited) {
            article.setAssociatedMovies(
                    articleServiceModel.getAssociatedMovies()
                            .stream()
                            .map(m -> this.modelMapper.map(m, Movie.class))
                            .collect(Collectors.toList())
            );
        }
        article.setContent(articleServiceModel.getContent());

        return this.modelMapper.map(this.articleRepository.saveAndFlush(article), ArticleServiceModel.class);
    }


    @Override
    public ArticleServiceModel editArticleWithEditedAssociatedMovies(String id, ArticleServiceModel articleServiceModel) {
        return editArticle(id, articleServiceModel, true);
    }

    @Override
    public ArticleServiceModel editArticleWithUneditedAssociatedMovies(String id, ArticleServiceModel articleServiceModel) {
        return editArticle(id, articleServiceModel, false);
    }

    @Override
    public ArticleServiceModel deleteArticle(String id) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article id not found!"));
        this.articleRepository.delete(article);
        return this.modelMapper.map(article, ArticleServiceModel.class);
    }
}
