package com.kyan7.cinephilia.integration.services;

import com.kyan7.cinephilia.domain.entities.Article;
import com.kyan7.cinephilia.repository.ArticleRepository;
import com.kyan7.cinephilia.service.ArticleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ArticleServiceTests {

    @Autowired
    ArticleService service;

    @MockBean
    ArticleRepository articleRepository;

    private List<Article> articles;

    @Before
    public void setupTest() {
        articles = new ArrayList<>();
        when(articleRepository.findAll())
                .thenReturn(articles);
    }

    @Test
    public void findAllArticles_whenArticles_returnArticles() {
        Article article = new Article();
        articles.add(article);

        var result = service.findAllArticles();
        assertEquals(1, result.size());
    }

}
