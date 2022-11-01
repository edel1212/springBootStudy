package org.zerock.mreview.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mreview.entity.MovieImage;

public interface MovieImageRepository extends JpaRepository<MovieImage,Long> {
}
