package org.zerock.mreview.repository.support;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.mreview.entity.Movie;

@Log4j2
public class MovieSupportRepositoryImpl extends QuerydslRepositorySupport  implements MovieSupportRepository{

    public MovieSupportRepositoryImpl() {
        super(Movie.class);
    }

    @Override
    public Page<Object[]> getListWithQuerydsl(Pageable pageable) {
        return null;
    }
}
