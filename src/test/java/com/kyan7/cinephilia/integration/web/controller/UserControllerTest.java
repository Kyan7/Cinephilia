package com.kyan7.cinephilia.integration.web.controller;

import com.kyan7.cinephilia.domain.entities.User;
import com.kyan7.cinephilia.repository.UserRepository;
import org.junit.Assert;
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

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void login_returnsCorrectView() throws Exception {
        this.mvc.perform(get("/users/login"))
                .andExpect(view().name("login"));
    }

    @Test
    public void register_returnsCorrectView() throws Exception {
        this.mvc.perform(get("/users/register"))
                .andExpect(view().name("register"));
    }

    @Test
    public void register_registersUserCorrectly() throws Exception {
        this.mvc.perform(post("/users/register")
                .param("username", "Test User")
                .param("password", "Testpassword123")
                .param("confirmPassword", "Testpassword123")
                .param("email", "test@email.com")
                .param("firstName", "Tester")
                .param("lastName", "Profileson"));
        Assert.assertEquals(1, this.userRepository.count());
    }

    @Test
    public void register_registerRedirectCorrectly() throws Exception {
        this.mvc.perform(post("/users/register")
                .param("username", "TestUser")
                .param("password", "Testpassword123")
                .param("confirmPassword", "Testpassword123")
                .param("email", "test@email.com")
                .param("firstName", "Tester")
                .param("lastName", "Profileson"))
                .andExpect(view().name("redirect:login"));
    }

    @Test
    @WithMockUser(username = "TestUser")
    public void profile_returnsCorrectView() throws Exception {
        //User user = new User();
        //user.setUsername("TestUser");
        //user.setPassword("Testpassword123");
        //user.setEmail("test@email.com");
        //user.setFirstName("Tester");
        //user.setLastName("Profileson");
        //user = this.userRepository.saveAndFlush(user);
//
//
        //this.mvc.perform(get("/profile").param())
        //        .andExpect(view().name("profile")).andExpect(model().attribute(""));
    }
}