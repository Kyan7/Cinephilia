package com.kyan7.cinephilia.domain.models.service;

import java.util.List;
import java.util.Set;

public class UserServiceModel {

    private String id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private List<GenreServiceModel> likedGenres;
    private List<ReviewServiceModel> reviews;

    private Set<RoleServiceModel> authorities;

    public UserServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<GenreServiceModel> getLikedGenres() {
        return likedGenres;
    }

    public void setLikedGenres(List<GenreServiceModel> likedGenres) {
        this.likedGenres = likedGenres;
    }

    public List<ReviewServiceModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewServiceModel> reviews) {
        this.reviews = reviews;
    }

    public Set<RoleServiceModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<RoleServiceModel> authorities) {
        this.authorities = authorities;
    }
}
