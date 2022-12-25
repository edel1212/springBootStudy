package org.zerock.bimovie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.bimovie.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @EntityGraph(attributePaths = "posterList", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Movie m")
    Page<Movie> findAll2(Pageable pageable);

    @Query("SELECT m , p, count(p) FROM Movie m LEFT JOIN Poster p " +
            "on p.movie = m GROUP BY p.movie")
    Page<Object[]> findAll3(Pageable pageable);

}
