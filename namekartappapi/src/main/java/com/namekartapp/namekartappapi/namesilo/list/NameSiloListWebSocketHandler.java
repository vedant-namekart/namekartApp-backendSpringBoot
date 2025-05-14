package com.namekartapp.namekartappapi.namesilo.list;

import com.google.gson.Gson;
import com.namekartapp.namekartappapi.AuctionList;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class NameSiloListWebSocketHandler extends TextWebSocketHandler {
    // List to hold all active WebSocket sessions
    private static final List<WebSocketSession> sessions = new ArrayList<>();

    // Gson instance to convert objects to JSON
    private static final Gson gson = new Gson();

    // This method is called when a new WebSocket connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Add the new session to the list of active sessions
        sessions.add(session);
        System.out.println("New NameSilo session established: " + session.getId());
    }

    // This method sends a list of auctions to all active WebSocket clients
    public static void sendAuctionUpdate(List<AuctionList> auctions) {
        // Convert the list of auctions into a JSON string
        String auctionListJson = gson.toJson(auctions);

        // Iterate through all active WebSocket sessions
        for (WebSocketSession session : sessions) {
            try {
                // Send the auction list as a TextMessage
                session.sendMessage(new TextMessage(auctionListJson));
                System.out.println("Auction list update sent to session: " + session.getId());
            } catch (Exception e) {
                System.err.println("Error sending NameSilo auction list update: " + e.getMessage());
            }
        }
    }

    // This method is called when a WebSocket connection is closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Remove the session from the list of active sessions
        sessions.remove(session);
        System.out.println("NameSilo session closed: " + session.getId());
    }
}
