package com.inpark.util;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class EchoHandler extends TextWebSocketHandler {
    //client의 연결된 객체정보를 담는다.
    private List<WebSocketSession> sessionList = new ArrayList<>();

    /* Connection 연결 되었을 때 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        sessionList.add(session);

        System.out.println("총 사용자 : " + sessionList.toString());

        System.out.println(session.getId() + " 사용자 연결 완료");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        System.out.println(session.getId() + "로부터 " +  message.getPayload() + "받음");

        /* 연결된 모든 클라이언트에게 메세지 전송 */
        for(WebSocketSession sess : sessionList){
            sess.sendMessage(new TextMessage(
            session.getPrincipal().getName() + "|" + message.getPayload()));
        }
    }

    /* Connection 연결 해제 되었을 때 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        sessionList.remove(session);

        System.out.println(session.getId() + " 사용자 연결 해제");
    }
}
