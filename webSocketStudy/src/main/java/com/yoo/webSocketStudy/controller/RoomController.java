package com.yoo.webSocketStudy.controller;

import com.yoo.webSocketStudy.dto.ChatRoomDTO;
import com.yoo.webSocketStudy.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Log4j2
public class RoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/chatRoom")
    public void room(){
        log.info("Chat Room List!!!");
    }

    //채팅방 목록 조회
    @GetMapping("/roomList")
    @ResponseBody
    public ResponseEntity<List<ChatRoomDTO>> rooms(){
        return ResponseEntity.ok(chatRoomRepository.findAllRooms());
    }

    //채팅방 개설
    @PostMapping("/room")
    @ResponseBody
    public String create(@RequestBody String name){
        log.info("# Create Chat Room , name: " + name);
        chatRoomRepository.createChatRoomDTO(name);
        return "success";
    }

    //채팅방 조회
    @GetMapping("/findRoom")
    @ResponseBody
    public void getRoom(String roomId){
        log.info(chatRoomRepository.findRoomById(roomId));
    }

}
