package com.kyan7.cinephilia.repository;

import com.kyan7.cinephilia.domain.entities.Movie;
import com.kyan7.cinephilia.domain.entities.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, String> {

    List<Screening> findAllByMovie(Movie movie);
}
