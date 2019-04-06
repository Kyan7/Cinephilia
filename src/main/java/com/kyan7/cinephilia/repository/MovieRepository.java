package com.kyan7.cinephilia.repository;

import com.kyan7.cinephilia.domain.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

    //TODO List<Movie> findAllByOrderByViews();
}
