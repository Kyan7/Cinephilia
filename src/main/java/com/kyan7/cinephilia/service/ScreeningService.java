package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.models.service.ScreeningServiceModel;

import java.util.List;

public interface ScreeningService {

    List<ScreeningServiceModel> findAllScreeningsByMovieId(String movieId);

    ScreeningServiceModel addScreening(ScreeningServiceModel screeningServiceModel);
}
