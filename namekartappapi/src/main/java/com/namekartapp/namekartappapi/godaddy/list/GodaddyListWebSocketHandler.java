package com.namekartapp.namekartappapi.godaddy.list;

import com.google.gson.Gson;
import com.namekartapp.namekartappapi.AuctionList;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class GodaddyListWebSocketHandler extends TextWebSocketHandler {
    // Maintain a list of WebSocket sessions to broadcast messages to
    private static final List<WebSocketSession> sessions = new ArrayList<>();
    private static final Gson gson = new Gson();

    // This method is called when a new WebSocket connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("New Godaddy All session established: " + session.getId());
    }

    // Method to send a list of auctions to all connected clients via WebSocket
    public static void sendAuctionListUpdate(List<AuctionList> auctions) {
        // Convert the list of auctions to JSON
        String auctionsJson = gson.toJson(auctions);

        // Send the JSON list to all connected clients
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(auctionsJson));  // Send the auctions list as a JSON string
                System.out.println("Sent auction list update to session " + session.getId());
            } catch (Exception e) {
                System.err.println("Error sending auction list update: " + e.getMessage());
            }
        }
    }

    // This method is called when the WebSocket connection is closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);  // Remove the session from the list
        System.out.println("Godaddy session closed: " + session.getId());
    }
}
