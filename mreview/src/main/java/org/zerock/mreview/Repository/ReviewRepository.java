package org.zerock.mreview.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMovie(Movie movie);

}
