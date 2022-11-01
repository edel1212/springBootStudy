package org.zerock.mreview.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mreview.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
