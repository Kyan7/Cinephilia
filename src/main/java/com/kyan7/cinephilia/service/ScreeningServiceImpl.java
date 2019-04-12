package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.entities.Movie;
import com.kyan7.cinephilia.domain.entities.Screening;
import com.kyan7.cinephilia.domain.models.service.ReviewServiceModel;
import com.kyan7.cinephilia.domain.models.service.ScreeningServiceModel;
import com.kyan7.cinephilia.repository.ScreeningRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;
    private final ModelMapper modelMapper;

    @Autowired
    public ScreeningServiceImpl(ScreeningRepository screeningRepository, MovieService movieService, ModelMapper modelMapper) {
        this.screeningRepository = screeningRepository;
        this.movieService = movieService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ScreeningServiceModel> findAllScreeningsByMovieId(String movieId) {
        Movie movie = this.modelMapper.map(this.movieService.findMovieById(movieId), Movie.class);
        return this.screeningRepository.findAllByMovie(movie)
                .stream()
                .map(s -> this.modelMapper.map(s, ScreeningServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ScreeningServiceModel addScreening(ScreeningServiceModel screeningServiceModel) {
        Screening screening = this.modelMapper.map(screeningServiceModel, Screening.class);
        return this.modelMapper.map(this.screeningRepository.saveAndFlush(screening), ScreeningServiceModel.class);
    }
}
