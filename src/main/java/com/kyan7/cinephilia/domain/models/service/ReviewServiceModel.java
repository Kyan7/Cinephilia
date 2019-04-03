package com.kyan7.cinephilia.domain.models.service;

public class ReviewServiceModel {

    private String id;
    private String title;
    private UserServiceModel reviewer;
    private long rating;
    private String description;
    private MovieServiceModel movie;
    private boolean isReported;
}
