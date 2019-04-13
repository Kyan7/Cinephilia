package com.kyan7.cinephilia.integration.web.controller;

import com.kyan7.cinephilia.repository.ArticleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @WithMockUser(value = "TestUser")
    public void list_returnsCorrectView() throws Exception {
        this.mvc.perform(get("/articles/list"))
                .andExpect(view().name("article/list-articles"));
    }

    @Test
    @WithMockUser(value = "TestUser")
    public void list_returnsCorrectAttribute() throws Exception {
        this.mvc.perform(get("/articles/list"))
                .andExpect(view().name("article/list-articles"))
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    @WithMockUser(value = "TestUser", roles = {"ADMIN"})
    public void all_returnsCorrectView() throws Exception {
        this.mvc.perform(get("/articles/all"))
                .andExpect(view().name("article/all-articles"));
    }

    @Test
    @WithMockUser(value = "TestUser", roles = {"ADMIN"})
    public void all_returnsCorrectAttribute() throws Exception {
        this.mvc.perform(get("/articles/all"))
                .andExpect(view().name("article/all-articles"))
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    @WithMockUser("TestUser")
    public void add_savesEntityCorrectly() throws Exception {
        //this.mvc.perform(post("/articles/add")
        //        .param("title", "Test Article").param("content");
    }
}
