package com.kyan7.cinephilia.integration.web.controller;

import com.kyan7.cinephilia.domain.entities.MovieTheater;
import com.kyan7.cinephilia.repository.MovieTheaterRepository;
import org.junit.Assert;
import org.junit.Before;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MovieTheaterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MovieTheaterRepository movieTheaterRepository;

    @Before
    public void emptyDb() {
        this.movieTheaterRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "TestUser")
    public void list_returnsCorrectView() throws Exception {
        this.mvc.perform(get("/movie-theaters/list"))
                .andExpect(view().name("movie-theater/list-movie-theaters"));
    }

    @Test
    @WithMockUser(value = "TestUser")
    public void list_returnsCorrectAttribute() throws Exception {
        MovieTheater first = new MovieTheater();
        first.setName("Test Movie Theater 1");
        first.setAddress("District 1, Street 1, Test Mall 1");
        first.setPhoneNumber("0888888881");
        first.setLink("https://www.kinotest.com/en/1234");
        first = this.movieTheaterRepository.saveAndFlush(first);

        MovieTheater second = new MovieTheater();
        second.setName("Test Movie Theater 2");
        second.setAddress("District, Street, Test Mall 2");
        second.setPhoneNumber("0888888882");
        second.setLink("https://www.cinematest.com/en/1234");
        second = this.movieTheaterRepository.saveAndFlush(second);

        this.mvc.perform(get("/movie-theaters/list"))
                .andExpect(view().name("movie-theater/list-movie-theaters"))
                .andExpect(model().attributeExists("movieTheaters"));
    }

    @Test
    @WithMockUser(value = "TestUser", roles = {"ADMIN"})
    public void all_returnsCorrectView() throws Exception {
        this.mvc.perform(get("/movie-theaters/all"))
                .andExpect(view().name("movie-theater/all-movie-theaters"));
    }

    @Test
    @WithMockUser(value = "TestUser", roles = {"ADMIN"})
    public void all_returnsCorrectAttribute() throws Exception {
        this.mvc.perform(get("/movie-theaters/all"))
                .andExpect(view().name("movie-theater/all-movie-theaters"))
                .andExpect(model().attributeExists("movieTheaters"));
    }

    @Test
    @WithMockUser(value = "TestUser", roles = {"ADMIN"})
    public void add_returnsCorrectView() throws Exception {
        this.mvc.perform(get("/movie-theaters/add"))
                .andExpect(view().name("movie-theater/add-movie-theater"));
    }

    @Test
    @WithMockUser(value = "TestUser", roles = {"ADMIN"})
    public void add_savesEntityCorrectly() throws Exception {
        this.mvc.perform(post("/movie-theaters/add")
                .param("name", "Test Movie Theater")
                .param("address", "District, Street, Test Mall")
                .param("phoneNumber", "0888888888")
                .param("link", "https://www.kinotest.com/en/1234"));
        Assert.assertEquals(1, movieTheaterRepository.count());

        MovieTheater actual = this.movieTheaterRepository.findAll().get(0);
        Assert.assertEquals("Test Movie Theater", actual.getName());
    }

    @Test
    @WithMockUser(value = "TestUser", roles = {"ADMIN"})
    public void add_failsDueToAlreadyExistingAddress() throws Exception {
        MovieTheater existingMovieTheater = new MovieTheater();
        existingMovieTheater.setName("Test Movie Theater");
        existingMovieTheater.setAddress("Same address");
        existingMovieTheater.setPhoneNumber("0888888888");
        existingMovieTheater.setLink("https://www.kinotest.com/en/1234");
        existingMovieTheater = this.movieTheaterRepository.saveAndFlush(existingMovieTheater);

        this.mvc.perform(post("/movie-theaters/add")
                .param("name", "New Movie Theater")
                .param("address", "Same address")
                .param("phoneNumber", "0123456789")
                .param("link", "https://www.kinodifferent.com/en/1234"));

        Assert.assertEquals(1, movieTheaterRepository.count());
    }



    @Test
    @WithMockUser(value = "TestUser", roles = {"ADMIN"})
    public void edit_savesEntityCorrectly() throws Exception {
        MovieTheater movieTheater = new MovieTheater();
        movieTheater.setName("Test Movie Theater");
        movieTheater.setAddress("District, Street, Test Mall");
        movieTheater.setPhoneNumber("0888888888");
        movieTheater.setLink("https://www.kinotest.com/en/1234");
        movieTheater = this.movieTheaterRepository.saveAndFlush(movieTheater);

        this.mvc.perform(post("/movie-theaters/edit/" + movieTheater.getId())
                .param("name", "Edited Movie Theater")
                .param("address", "New address")
                .param("link", "https://www.editedkinotest.com/en/5678"));

        MovieTheater actual = this.movieTheaterRepository.findById(movieTheater.getId()).orElse(null);
        Assert.assertEquals("Edited Movie Theater", actual.getName());
    }

    @Test
    @WithMockUser(value = "TestUser", roles = {"ADMIN"})
    public void delete_deletesEntityCorrectly() throws Exception {
        MovieTheater first = new MovieTheater();
        first.setName("Test Movie Theater 1");
        first.setAddress("District 1, Street 1, Test Mall 1");
        first.setPhoneNumber("0888888881");
        first.setLink("https://www.kinotest.com/en/1234");
        first = this.movieTheaterRepository.saveAndFlush(first);

        MovieTheater second = new MovieTheater();
        second.setName("Test Movie Theater 2");
        second.setAddress("District, Street, Test Mall 2");
        second.setPhoneNumber("0888888882");
        second.setLink("https://www.cinematest.com/en/1234");
        second = this.movieTheaterRepository.saveAndFlush(second);

        this.mvc.perform(post("/movie-theaters/delete/" + first.getId()));
        Assert.assertEquals(1,movieTheaterRepository.count());
    }
}
