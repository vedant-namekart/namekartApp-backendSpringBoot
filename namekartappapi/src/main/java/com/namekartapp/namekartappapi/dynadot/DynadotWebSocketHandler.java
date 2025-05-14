package com.namekartapp.namekartappapi.dynadot;

import com.google.gson.Gson;
import com.namekartapp.namekartappapi.Auction;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class DynadotWebSocketHandler extends TextWebSocketHandler {
    private static final List<WebSocketSession> sessions = new ArrayList<>();
    private static final Gson gson = new Gson();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("New Dynadot session established: " + session.getId());
    }

    public static void sendAuctionUpdate(Auction auction) {
        String auctionJson = gson.toJson(auction);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(auctionJson));
            } catch (Exception e) {
                System.err.println("Error sending Dynadot auction update: " + e.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("Dynadot session closed: " + session.getId());
    }
}
