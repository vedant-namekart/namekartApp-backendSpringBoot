package com.namekartapp.namekartappapi.auctions.watchlist;

import com.google.gson.Gson;
import com.namekartapp.namekartappapi.Auction;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class WatchlistWebSocketHandler extends TextWebSocketHandler {
    private static  final List<WebSocketSession> sessions=new ArrayList<>();

    private static final Gson gson=new Gson();

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.add(session);
        System.out.println("New WatchList Session Established: "+session.getId());

    }

    public static void sendAuctionUpdate(List<Auction> auction){
        String auctionListJson=gson.toJson(auction);
        for(WebSocketSession session:sessions){
            try{
                session.sendMessage(new TextMessage(auctionListJson));
            }catch (Exception e){
                System.out.println("Error sending AuctionList Update: "+e.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        sessions.remove(session);
        System.out.println("WatchList session closed: " + session.getId());

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println(message.getPayload()+"");
    }
}
