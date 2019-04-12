package com.kyan7.cinephilia.repository;

import com.kyan7.cinephilia.domain.entities.MovieTheater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieTheaterRepository extends JpaRepository<MovieTheater, String> {
}
