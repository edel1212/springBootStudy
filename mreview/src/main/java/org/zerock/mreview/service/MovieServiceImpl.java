package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.repository.MovieRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    @Override
    public Long register(MovieDTO movieDTO) {
        //TODO  DTO -> Entity -> Repository
        return null;
    }
}
