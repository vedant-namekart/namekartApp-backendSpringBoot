package com.namekartapp.namekartappapi.namecheap.list;

import com.google.gson.Gson;
import com.namekartapp.namekartappapi.AuctionList;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class NamecheapListWebSocketHandler extends TextWebSocketHandler {
    // Maintain a list of active WebSocket sessions
    private static final List<WebSocketSession> sessions = new ArrayList<>();

    // Use Gson to convert objects to JSON
    private static final Gson gson = new Gson();

    // This method is called when a new WebSocket connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("New Namecheap session established: " + session.getId());
    }

    // This method sends auction updates (a list of auctions) to all connected clients
    public static void sendAuctionUpdate(List<AuctionList> auctions) {
        // Convert the list of auctions into JSON format
        String auctionListJson = gson.toJson(auctions);

        // Iterate over all active WebSocket sessions and send the auction update
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(auctionListJson));
                System.out.println("Auction list update sent to session: " + session.getId());
            } catch (Exception e) {
                System.err.println("Error sending Namecheap auction list update: " + e.getMessage());
            }
        }
    }

    // This method is called when a WebSocket connection is closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("Namecheap session closed: " + session.getId());
    }
}
