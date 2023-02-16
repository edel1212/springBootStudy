package org.zerock.mreview.repository.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieSupportRepository {
    Page<Object[]> getListWithQuerydsl(Pageable pageable);
}
