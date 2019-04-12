package com.kyan7.cinephilia.domain.models.view;

public class ScreeningViewModel {

    private String id;
    private String MovieTheater;
    private String type;
    private Double price;
    private String timeStamp;

    public ScreeningViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieTheater() {
        return MovieTheater;
    }

    public void setMovieTheater(String movieTheater) {
        MovieTheater = movieTheater;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
