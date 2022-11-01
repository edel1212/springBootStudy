package org.zerock.mreview.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mreview.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
