package com.yoo.statistics.service;

import com.yoo.statistics.dto.HeadquartersDTO;
import org.springframework.web.multipart.MultipartFile;

public interface HeadquartersService {
    void registerHeadquarters(HeadquartersDTO headquartersDTO,MultipartFile uploadFile);
}
