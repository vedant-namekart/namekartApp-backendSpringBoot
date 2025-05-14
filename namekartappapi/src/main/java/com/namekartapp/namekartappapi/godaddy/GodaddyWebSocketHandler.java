package com.namekartapp.namekartappapi.godaddy;

import com.google.gson.Gson;
import com.namekartapp.namekartappapi.Auction;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class GodaddyWebSocketHandler extends TextWebSocketHandler {
    private static final List<WebSocketSession> sessions = new ArrayList<>();
    private static final Gson gson = new Gson();



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("Godaddy session closed: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println(message.getPayload()+" is clicked on godaddy");
    }
}
