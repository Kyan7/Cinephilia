package com.kyan7.cinephilia.repository;

import com.kyan7.cinephilia.domain.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, String> {

    List<Genre> findAllByOrderByNameAsc();
}
