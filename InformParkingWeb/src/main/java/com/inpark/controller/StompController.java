package com.inpark.controller;

import com.inpark.dto.WsMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
public class StompController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/stompnoti")
    @SendTo("/topic/message")
    public WsMessageDto sendMsg(WsMessageDto msg) throws Exception{
        System.out.println("Controller msg : " + msg);
        return msg;
       /* HashMap<String, Object> payload = new HashMap<>();
        payload.put("name", "changhee");
        simpMessagingTemplate.convertAndSend("/topic/a",payload); */
    }
}
