package com.test.task.repository;

import com.test.task.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query(value = "SELECT * FROM movie WHERE id NOT IN (:favourites) ORDER BY id ASC",
            nativeQuery = true)
    Set<Movie> findAllUnfavourites(@Param("favourites") Set<Long> favourites);

    @Query(value = "select * from movie where title = :title", nativeQuery = true)
    Optional<Movie> findByTitle(String title);
}
