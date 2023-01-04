package com.yoo.toy.redirectController;

import com.yoo.toy.dto.PersonDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Controller
@RequestMapping("/sample")
public class RedirectTestController {

    @GetMapping("/t1")
    public String redirectAddFlash(RedirectAttributes redirectAttributes){
        log.info("addFlash!!!");

        //Data reject
        redirectAttributes.addFlashAttribute("flag",true);

        PersonDTO personDTO = PersonDTO.builder()
                .name("yoo")
                .age(30)
                .gender(true)
                .build();

        redirectAttributes.addFlashAttribute("person", personDTO);

        List<PersonDTO> personArr = IntStream.rangeClosed(1, 100)
                .mapToObj(i ->{
                    return PersonDTO.builder()
                            .name("yoo"+i)
                            .age(i)
                            .gender(i % 2 == 0)
                            .build();
                }).collect(Collectors.toList());

        redirectAttributes.addFlashAttribute("personArr", personArr);

        redirectAttributes.addFlashAttribute("list", List.of(1,2,3,4));

        return "redirect:/sample/target";
    }

    @GetMapping("/t2")
    public String getMappingTarget(RedirectAttributes redirectAttributes){
        log.info("addAttributes!!");

        //Data reject
        redirectAttributes.addAttribute("flag",true);

//        PersonDTO personDTO = PersonDTO.builder()
//                .name("yoo")
//                .age(30)
//                .gender(true)
//                .build();
//
//        redirectAttributes.addAttribute("person", personDTO);
//
//        List<PersonDTO> personArr = IntStream.rangeClosed(1, 100)
//                .mapToObj(i ->{
//                    return PersonDTO.builder()
//                            .name("yoo"+i)
//                            .age(i)
//                            .gender(i % 2 == 0)
//                            .build();
//                }).collect(Collectors.toList());
//
//        redirectAttributes.addAttribute("personArr", personArr);
      //  redirectAttributes.addAttribute("list", List.of(1,2,3,4));

        return "redirect:/sample/target";
    }

    @GetMapping("t3")
    public String addAttrbuesTest2(RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("result", "가나다라");
        return "redirect:/sample/target2";
    }

    //Redirect를 받을 Controller
    @GetMapping("/target")
    public void redirectTarget(Boolean flag, Model model){
        log.info("flag ::: " + flag);
        //model.addAttribute("flag", flag);
        log.info("I'm Redirect Target!");
    }

    @GetMapping("/target2")
    public void redirectTarget2(){
        //model.addAttribute("flag", flag);
        log.info("I'm Redirect Target2!");
    }
}
