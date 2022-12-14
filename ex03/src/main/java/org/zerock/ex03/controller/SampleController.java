package org.zerock.ex03.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.ex03.DTO.SampleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * @Description  : 기본적으로 Spring 과 비슷하다 void의 메서드일경우 해당 URL에 따라 Directory 및 html 파일명이 정해진다
 *                 그리고 파일 시작 위치는 /resources/templates 이다
 *
 *                 ✔ 기본적으로 html 을 사용하기 위해서는 thymeleaf 를 build.gradle 에 추가해줘야한다
 *                 ✔ jsp 를 사용하고 싶다면 application.properties 에 설정을 추가해줘야한다!
 *
 *                 ✔ SpringBoot 에서는 기본적으로 Lombod 라이브러리 중 하나인 @Log4j2 를 기본적으로 사용하기떄문에
 *                    별도의 설정없이 사용이 가능하다.
 * */
@Controller
@RequestMapping("/sample")
@Log4j2
public class SampleController {

    @GetMapping("/ex1")
    public void ex1(){
        log.info("ex1..... This is Get type !");
    }

    @GetMapping({"/ex2"})
    public void exModel(Model model){
        /**
         * IntStream.range() , IntStream.rangeClosed() 차이
         *
         * - range() : 마지막 숫자는 포함 X
         *
         * - rangeClosed() : 마지막 숫자를 포함한다!
         *
         * */
        List<SampleDTO> list = IntStream.rangeClosed(1,20)
                                .asLongStream().mapToObj(i->{
                    return SampleDTO.builder()
                            .sno(i)
                            .first("First" + i)
                            .last("last" + i)
                            .regTime(LocalDateTime.now())
                            .build();
                }).collect(Collectors.toList());

        model.addAttribute("list" ,  list);
    }

    /**
     * @Description :  Inline 기능에서 주의 깊게 봐야 하는것은 javascript 부분이다.
     * 
     *                 ✔ <script th:inline="javascript">
     *                     let dto = [[${dto}]]
     *                   </script>
     *                   설정만으로 다른 설정없이 JSON 데이터로 받아서
     *                   사용이 가능하다
     *                   
     *                 🎈 주의사항 : addAttribute() 와 addFlashAttribute() 의 차이점은 
     *                             해당 redirect로 페이지가 이동 됐을 시 
     *                              - addFlashAttribute() 경우 URL 데이터가 남지 않고 redirect를 받은 화면에서 해당
     *                               전달해준 데이터를 사용 가능 [ 일회성 - URL에 기록이 남지않음 (POST와 흡사함) ]
     * 
     *                              - addAttribute()의 경우 데이터 값이 URL에 남으며 해당 값을 Target Contoller에서 
     *                                다시 받아줘야한다. [ 다회성 가능 새로고침해도 URL은 남아있기 때문임 (GET 방식) ]
     *                               ❌ 또한 객체 형태를 addAttribute()로 넘기면 Error!
     *                                  Error Msg : Failed to convert value of type 'org.zerock.ex03.DTO.SampleDTO' to required type 'java.lang.String';
     *                                              nested exception is java.lang.IllegalStateException: Cannot convert value of type 'org.zerock.ex03.DTO.SampleDTO'
     *                                              to required type 'java.lang.String': no matching editors or conversion strategy found
     * */
    @GetMapping({"/exInline"})
    public String exInline(RedirectAttributes redirectAttributes){
        log.info("exInline .... This used RedirectAttributes ");
        SampleDTO dto = SampleDTO.builder().sno(100L).first("First 100").last("Last 100").regTime(LocalDateTime.now()).build();
        redirectAttributes.addFlashAttribute("result","success");
        redirectAttributes.addFlashAttribute("dto",dto);        
        // 해당 URL 접근 시 데이터를 포함하여 redirect 시켜버림
        return "redirect:/sample/ex3";
    }

    /**
     * @Description :  상위 exInline() 와 비교
     *               - exInline                 : redirectAttributes.addFlashAttribute("Key" , value); [일회성]
     *               - compareAttrWithFlashAttr : redirectAttributes.addAttribute("Key" , value);     [다회 - 받는쪽에서 다시 Model로 보내줘야함. ]
     */
    @GetMapping("/compareAttr")
    public String compareAttrWithFlashAttr(RedirectAttributes redirectAttributes){

        log.info("Compare with Attr");

        log.info("exInline .... This used RedirectAttributes ");
        SampleDTO dto = SampleDTO.builder().sno(100L).first("First 100").last("Last 100").regTime(LocalDateTime.now()).build();
        redirectAttributes.addAttribute("result","success");
        redirectAttributes.addAttribute("dto",dto);

        return "redirect:/sample/ex3";
    }

    @GetMapping("/ex3")
    public void ex3(){
        log.info("ex3 ............ I'm GetMapping ");
    }

    @GetMapping({"/exLink"})
    public void exLinkModel(Model model){
        List<SampleDTO> list = LongStream.rangeClosed(0,10).mapToObj(i->{
            return SampleDTO.builder().sno(i).first("first"+i).last("last"+ i).regTime(LocalDateTime.now()).build();
        }).collect(Collectors.toList());
        model.addAttribute("list",list);
    }

    @GetMapping({"/exLayout1","/exLayout2","/exTemplate","/exSidebar"})
    public void exLayout1(){
        log.info("exLayout!!");
    }

}
