package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

@Log4j2
@RequestMapping("/board/")
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    
    //목록 조회
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list ::::" + pageRequestDTO);
        model.addAttribute("result",boardService.getList(pageRequestDTO));
    }
    
    //go to Page
    @GetMapping("/register")
    public void register(){
        log.info("go to register Page");
    }
    
    //등록
    @PostMapping("/register")
    public String registerPost(BoardDTO  dto, RedirectAttributes redirectAttributes){
        /*
         * @Description : 클라이언트 단에서 값을 넘겨줄때
         *                email(작성자) 값은 FK(외래키)로 지정 되어
         *                있기 때문에 Member에 등록되어 있는 값을 넣어줘야하!!
         *
         *                안되서 왜 안되지 하고 삽질함 ...
         *
         * */
        log.info("post Register");
        Long bno = boardService.register(dto);
        log.info("register Success bno :: " + bno);
        redirectAttributes.addFlashAttribute("msg",bno);
        return "redirect:/board/list";
    }

}
