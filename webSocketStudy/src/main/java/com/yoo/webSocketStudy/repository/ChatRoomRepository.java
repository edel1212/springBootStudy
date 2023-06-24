package com.yoo.webSocketStudy.repository;

import com.yoo.webSocketStudy.dto.ChatRoomDTO;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {

    private Map<String, ChatRoomDTO> chatRoomDTOMap;

    @PostConstruct
    private void init(){
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    // 모든 방 찾기
    public List<ChatRoomDTO> findAllRooms(){
        //채팅방 생성 순서 최근 순으로 반환
        List<ChatRoomDTO> result = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(result);
        return result;
    }

    // 방 찾기
    public ChatRoomDTO findRoomById(String id){
        return chatRoomDTOMap.get(id);
    }

    // 채팅 방 생성
    public ChatRoomDTO createChatRoomDTO(String name){
        ChatRoomDTO room = new ChatRoomDTO(name);
        chatRoomDTOMap.put(room.getRoomId(), room);
        return room;
    }

}
