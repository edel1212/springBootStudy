package com.yoo.toy.controller;

import com.yoo.toy.dto.GuestBookDTO;
import com.yoo.toy.dto.PageRequestDTO;
import com.yoo.toy.dto.PageResultDTO;
import com.yoo.toy.entity.GuestBook;
import com.yoo.toy.entity.QGuestBook;
import com.yoo.toy.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/paging")
@Log4j2
@RequiredArgsConstructor
public class PagingController {

    private final GuestbookService guestbookService;

    // PageRequestDTO 기본 생성자로 값이 들어가있음!
    @GetMapping("/view")
    public void pageView(PageRequestDTO pageDTO, Model model){
        log.info("Paging View Test");

        PageResultDTO<GuestBookDTO, GuestBook> result = guestbookService.getList(pageDTO);

        model.addAttribute("result", result);
    }

    @GetMapping("/test")
    public void modelAttributeTest(@ModelAttribute("modelTestData") PageRequestDTO dto){
        log.info("dto :: {}", dto);
    }

    //redirect를 섞어서 사용
    @PostMapping("/direct")
    public String modelAttrWithRedirect(@ModelAttribute("modelTestData") PageRequestDTO dto
                , RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("page",dto.getPage());
        redirectAttributes.addFlashAttribute("keyword",dto.getKeyword());
        redirectAttributes.addFlashAttribute("type",dto.getType());
        return "redirect:/paging/view";
    }


}
