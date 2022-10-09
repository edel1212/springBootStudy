package com.yoo.guestbook.controller;

import com.yoo.guestbook.dto.PageRequestDTO;
import com.yoo.guestbook.dto.PageResultDTO;
import com.yoo.guestbook.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service;

    @GetMapping("/")
    public String index(){
        log.info("root Page");
        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list....." + pageRequestDTO);
        model.addAttribute("result",service.getList(pageRequestDTO));
    }

}
