package com.namekartapp.namekartappapi.dropcatch.list;

import com.google.gson.Gson;
import com.namekartapp.namekartappapi.AuctionList;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class DropCatchListWebSocketHandler extends TextWebSocketHandler {

    // List to store active WebSocket sessions
    private static final List<WebSocketSession> sessions = new ArrayList<>();
    private static final Gson gson = new Gson();  // Gson instance to convert AuctionList objects to JSON

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Add the new session to the list
        sessions.add(session);
        System.out.println("New DropCatch session established: " + session.getId());
    }

    /**
     * Sends auction updates to all connected WebSocket sessions.
     * Supports both single auction or a list of auctions.
     *
     * @param auctions List of AuctionList objects to send
     */
    public static void sendAuctionUpdate(List<AuctionList> auctions) {
        String auctionsJson = gson.toJson(auctions);  // Convert the list of auctions to JSON
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(auctionsJson));  // Send the message as a JSON string
                System.out.println("Sent auction list update to session: " + session.getId());
            } catch (Exception e) {
                System.err.println("Error sending auction list update to session " + session.getId() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Sends a single auction update to all connected WebSocket sessions.
     *
     * @param auction Single AuctionList object to send
     */
    public static void sendAuctionUpdate(AuctionList auction) {
        String auctionJson = gson.toJson(auction);  // Convert the single auction to JSON
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(auctionJson));  // Send the message as a JSON string
                System.out.println("Sent single auction update to session: " + session.getId());
            } catch (Exception e) {
                System.err.println("Error sending single auction update to session " + session.getId() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Remove the session from the list when it is closed
        sessions.remove(session);
        System.out.println("DropCatch session closed: " + session.getId());
    }
}
