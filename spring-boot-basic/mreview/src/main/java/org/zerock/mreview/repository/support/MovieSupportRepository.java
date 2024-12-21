package org.zerock.mreview.repository.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieSupportRepository {
    Page<Object[]> getListWithQuerydsl(Pageable pageable);

    List<Object[]> testGetMovieWithAllQuerydls(Long mno);
}
