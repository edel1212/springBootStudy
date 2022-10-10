package com.yoo.guestbook.controller;

import com.yoo.guestbook.dto.GuestbookDTO;
import com.yoo.guestbook.dto.PageRequestDTO;
import com.yoo.guestbook.dto.PageResultDTO;
import com.yoo.guestbook.service.GuestbookService;
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

    /////////////////////////////////////////////////////////

    @GetMapping("/register")
    public void register(){
        log.info("register!!");
    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO guestbookDTO, RedirectAttributes redirectAttributes){

        log.info("guestbookDTO ::: ... " + guestbookDTO);

        Long gno = service.register(guestbookDTO);
        
        //redirect 시키면서 등록된 gno를 넘겨줌
        redirectAttributes.addFlashAttribute("msg",gno);

        return "redirect:/guestbook/list";
    }

    /**
     *  @Description  : @ModelAttribute("requestDTO") PageRequestDTO requestDTO 를 사용하는 이유는
     *                  현재 로직에서는 없어 되지만 나중에 다시 목록 페이지로 돌아갈때 데이터를 같이 젖아히기 위해서
     *                  명시해 두었음!
     *
     *                  ✔ PageRequestDTO 에는 page , size 가 들어있는데
     *                    이것을 parameter 로 받는 동시에
     *                    @ModelAttribute("requestDTO") 를 통해 requestDTO 에서 지정한 값을 자동으로 binding 되어
     *                    view 단에서 사용함 ${requestDTO.page} , ${requestDTO.size} 와 같이 사용함
     *
     * */
    @GetMapping({"/read","/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model ) {
        log.info("gno :: " + gno);
        log.info("pageRequestDTO :: " + pageRequestDTO);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto",dto);
    }

    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes){
        log.info("remove Page");
        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg",gno);
        return "redirect:/guestbook/list";
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO guestbookDTO, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO , RedirectAttributes redirectAttributes){
        log.info("post modify");

        service.modify(guestbookDTO);

        /**
         * @Description  : read Controller 를 보면 받는 부분이 long gno 와
         *                 @ModelAttribute("requestDTO) 를 사용해서
         *                 받지만 아래의 응답 값은 key 로 던져주는데 받을 수 있는 이유는
         *                 해당 아래 방법으로 던질경우 URL을 살펴보면
         *
         *                 ~~ /modify?gno=170&page=17&type=tc&keyword=title 와 같은 경우로 넘겨줘서
         *                 알아서 binding 되어서 view 단에서  ${requestDTO.type}와 같이
         *                 사용이 가능했던 것이다
         * */
        redirectAttributes.addAttribute("page",pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type",pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword",pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("gno",guestbookDTO.getGno());
        return "redirect:/guestbook/read";
    }

}
