package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.board.service.BoardService;

@Log4j2
@RequestMapping("/board/")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

}
