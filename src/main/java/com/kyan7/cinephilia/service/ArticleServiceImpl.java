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

    /**
     * Finds all articles in the database.
     * @return list of article service models.
     */
    @Override
    public List<ArticleServiceModel> findAllArticles() {
        return this.articleRepository.findAll()
                .stream()
                .map(a -> this.modelMapper.map(a, ArticleServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Attempts to add an article to the database.
     * @param articleServiceModel transfers the article's data to the method.
     * @return a respective model of the article.
     */
    @Override
    public ArticleServiceModel addArticle(ArticleServiceModel articleServiceModel) {
        Article article = this.articleRepository.findByTitle(articleServiceModel.getTitle())
                .orElse(null);
        if (article != null) {
            throw new IllegalArgumentException("Article already exists!");
        }
        article = this.modelMapper.map(articleServiceModel, Article.class);
        this.articleRepository.saveAndFlush(article);
        return this.modelMapper.map(article, ArticleServiceModel.class);
    }

    /**
     * Attempts to find an article and increase its views by 1.
     * @param id is the id of the article we're searching for.
     * @return a respective model of the article.
     */
    @Override
    public ArticleServiceModel findArticleByIdAndIncrementViews(String id) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found!"));
        article.setViews(article.getViews() + 1);
        this.articleRepository.saveAndFlush(article);
        return this.modelMapper.map(article, ArticleServiceModel.class);
    }

    /**
     * Attempts to find an article.
     * @param id is the id of the article we're searching for.
     * @return a respective model of the article.
     */
    @Override
    public ArticleServiceModel findArticleById(String id) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found!"));
        return this.modelMapper.map(article, ArticleServiceModel.class);
    }

    /**
     * Attempts to edit an article. Depending on whether it's had its associated movies edited the method chooses whether to set new ones.
     * @param id is the id of the article we're editing.
     * @param articleServiceModel transfers the article's new data to the method.
     * @param isAssociatedMoviesEdited shows us whether there the article's associated movies have been changed.
     * @return a respective model of the article.
     */
    @Override
    public ArticleServiceModel editArticle(String id, ArticleServiceModel articleServiceModel, boolean isAssociatedMoviesEdited) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found!"));
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


    /**
     * Attempts to edit an article which has had its associated movies changed.
     * @param id is the id of the article we're editing.
     * @param articleServiceModel transfers the article's new data to the method.
     * @return a respective model of the article.
     * @see #editArticle(String, ArticleServiceModel, boolean)
     */
    @Override
    public ArticleServiceModel editArticleWithEditedAssociatedMovies(String id, ArticleServiceModel articleServiceModel) {
        return editArticle(id, articleServiceModel, true);
    }

    /**
     * Attempts to edit an article which hasn't had its associated movies changed.
     * @param id is the id of the article we're editing.
     * @param articleServiceModel transfers the article's new data to the method.
     * @return a respective model of the article.
     * @see #editArticle(String, ArticleServiceModel, boolean)
     */
    @Override
    public ArticleServiceModel editArticleWithUneditedAssociatedMovies(String id, ArticleServiceModel articleServiceModel) {
        return editArticle(id, articleServiceModel, false);
    }

    /**
     * Attempts to delete an article.
     * @param id is the id of the article we're deleting.
     * @return a respective model of the article.
     */
    @Override
    public ArticleServiceModel deleteArticle(String id) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found!"));
        this.articleRepository.delete(article);
        return this.modelMapper.map(article, ArticleServiceModel.class);
    }
}
