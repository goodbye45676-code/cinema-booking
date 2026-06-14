package com.movie.bookingSystem.repository;

import com.movie.bookingSystem.model.Movie;
import org.hibernate.boot.models.JpaAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

}
