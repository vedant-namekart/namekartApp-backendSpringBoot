package com.namekartapp.namekartappapi;

import com.google.api.client.json.Json;
import com.google.gson.*;
import org.hibernate.dialect.JsonHelper;
import org.hibernate.dialect.PostgreSQLJsonPGObjectJsonbType;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.namekartapp.namekartappapi.ProgramHelper.doAllSendingAndDatabaseAddingTask;
import static com.namekartapp.namekartappapi.ProgramHelper.getSubCollectionsForPath;

@Component
public class DomainWebSocketHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private static final Map<String, WebSocketSession> userSessions = new HashMap<>();
    private static final Map<String, List<String>> mismatchedCollectionsCache = new HashMap<>();

    private static final Map<String, Set<String>> mismatchedCollectionsByUser = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Extract userId from the query parameters
        String query = session.getUri().getQuery();
        Map<String, String> queryParams = UriComponentsBuilder.fromUriString("?" + query).build().getQueryParams().toSingleValueMap();
        String userId = queryParams.get("userId");

        if (userId != null) {
            // Set the userId in the session attributes
            session.getAttributes().put("userId", userId);
            userSessions.put(userId, session);
            System.out.println("WebSocket connection established for user: " + userId + " (sessionId: " + session.getId() + ")");
        } else {
            System.out.println("No user ID provided for WebSocket connection (sessionId: " + session.getId() + ")");
        }

        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message from client (Same) " + session.getId() + ": " + payload);


        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();



        // Log the userId along with the sessionId
        String userId = getUserIdFromSession(session); // Retrieve userId from session
        System.out.println("Message from user " + userId + " (sessionId: " + session.getId() + "): " + payload);


        if(jsonObject.has("place a bid")){
            JsonObject response=new JsonObject();
            response.addProperty("status","success");
            response.addProperty("bidPlacedAmount", jsonObject.get("place a bid").toString());
            sendMessageToUser(userId,response.toString());
        }
        else if(jsonObject.toString().contains("sendtoserver")){
            String messageToSend=jsonObject.get("sendtoserver").getAsString();
            String calledDocumentPath=jsonObject.get("calledDocumentPath").getAsString();
            String calledDocumentPathFields=jsonObject.get("calledDocumentPathFields").getAsString();
            String type=jsonObject.get("type").toString();
            Map<String, Object> newResponse=null;

            System.out.println(calledDocumentPath);

            if(type.contains("stats")) {
            System.out.println("sdsf");
                newResponse = Map.of(
                        "text", Map.of(
                                "h1", "updated.com",
                                "h2", "Age:10 | EST:20 | GDV:1220",
                                "h3", "CB: 3.0 OB:0 | RN: NUll"
                        )
                );
            }else if (type.equals("leads")){

                newResponse = Map.of(
                        "text", Map.of(
                                "h1", "Livy",
                                "h2", "Unfiltered Active Leads of friendly.solutions:\n",
                                "h3", "friendly.com>>friendly.net>>friendly.co.uk>>friendly.ru>>friendly.cn>>friendly.nl>>friendly.eu>>friendly.it>>friendly.ca>>friendly.com.au>>friendly.ch>>friendly.co>>friendly.us>>friendly.pl>>friendly.in>>friendly.com.cn>>friendly.be>>friendly.at>>friendly.cz>>friendly.uk>>friendly.biz>>friendly.dk>>friendly.jp>>friendly.io>>friendly.pro>>friendly.app>>friendly.hu>>friendly.no>>friendly.lol>>friendly.sk>>friendly.click>>friendly.au>>friendly.com.mx>>friendly.cl>>friendly.co.jp>>friendly.com.tr>>friendly.co.nz>>friendly.gr>>friendly.com.ar>>friendly.fi>>friendly.icu>>friendly.cloud>>friendly.org.uk>>friendly.asia>>friendly.ai>>friendly.mx>>friendly.space>>friendly.buzz>>friendly.world>>friendly.pt>>friendly.le>>friendly.com.ua>>friendly.com.pl>>friendly.co.il>>friendly.vn>>friendly.kr>>friendly.group>>friendly.tokyo>>friendly.lt>>friendly.ae>>friendly.digital>>friendly.su>>friendly.studio>>friendly.s>>friendly.nz>>friendly.my>>friendly.com.co>>friendly.design>>friendly.by>>friendly.lv>>friendly.ws>>friendly.skin>>friendly.ma>>friendly.lu>>friendly.media>>friendly.news>>friendly.com.sg>>friendly.pe>>friendly.uz>>friendly.is>>friendly.ny<>>friendly.global>>friendly.pk>>friendly.li>>friendly.monster>>friendly.bio>>friendly.la>>friendly.social>>friendly.com.pe>>friendly.chat>>friendly.beauty>>friendly.boats>>friendly.com.ng>>friendly.ge>>friendly.fit>>friendly.earth>>friendly.centerfriendly.com.uy>>fri\n" +
                                        "endly.consulting>>friendly.support>>friendl\n" +
                                        ">>friendly.wtf>>friendly.lk>>friendly.org.in>>friendly.im>>frie >>friendly.pizza>>friendly.events>>friendly.gmbh>>friendly\n" +
                                        "friendly.web.id>>friendly.vc>>friendly.education>>friendly.works>>friendly.expert>>friendly.marketing>>friendly.paby>>friendly.coach>>friendly.travel>>friendly.in.ua>>friendly.sh>>friendly.house>>friendly.software>>friendly.swiss>>friendly.school>>friendly.tips>>friendly.cool>>friendly",
                                "h4", "Filtered Active Leads:\n",
                                "h5", "friendly.ru,friendly.bio,friendly.earth,friendly.cerer.friendly.biz.friendly.eco,friendly.global,friendly.io,friendly.studio,friendly.cloud,friendly.show,friendly.market,friendly.la,friendly.hu,friendly.sk,friendly.com.ua,friendly.ch,friendly.pe,friendly.skin,friendly.ma,friendly.dire\n"
                        )
                );

            }else if(type.equals("search")){
                newResponse = Map.of(
                        "url", Map.of(
                                "google", "google.com",
                                "google", "google.com",
                                "google", "google.com",
                                "google", "google.com",
                                "google", "google.com"
                        )
                );
            }

            Map<String, Object> updated = ProgramHelper.updateFirestoreByPath(
                    calledDocumentPath,
                    calledDocumentPathFields,
                    newResponse
            );


            broadcastMessage("broadcast-update",calledDocumentPath,updated);


        }
        else if(jsonObject.get("query").toString().contains("getWatchList")){

            Auction tempAuction1 = new Auction(
                    "auction123", 1000, "2025-03-06T12:00:00", 5, 10, 25, 500,
                    "example.com", 1, "2025-03-07T12:00:00", System.currentTimeMillis(),
                    "Appraisal data", 5000, 2, "auction123-id", "1000", 1, "dynadot",
                    "1 day left", "utf-example", "live", 100, "Appraisal info", 3, 1, 2, 0, 1
            );

            Auction tempAuction2 = new Auction(
                    "auction456", 2000, "2025-03-08T12:00:00", 3, 8, 15, 700,
                    "example2.com", 2, "2025-03-09T12:00:00", System.currentTimeMillis(),
                    "Appraisal data 2", 6000, 3, "auction456-id", "2000", 2, "namesilo",
                    "2 days left", "utf-example2", "live", 150, "Appraisal info 2", 4, 2, 1, 0, 1
            );

            JsonArray auctionArray = new JsonArray();

            JsonObject auctionJson1 = new JsonObject();

            auctionJson1.addProperty("domain", tempAuction1.getDomain());
            auctionJson1.addProperty("auction_id", tempAuction1.getAuction_id());
            auctionJson1.addProperty("age", tempAuction1.getAge());
            auctionJson1.addProperty("est", tempAuction1.getEst());
            auctionJson1.addProperty("gdv", tempAuction1.getGdv());
            auctionJson1.addProperty("extns", tempAuction1.getExtns());
            auctionJson1.addProperty("lsv", tempAuction1.getLsv());
            auctionJson1.addProperty("cpc", tempAuction1.getCpc());
            auctionJson1.addProperty("bidders", tempAuction1.getBidders());
            auctionJson1.addProperty("platform", tempAuction1.getPlatform());
            auctionJson1.addProperty("endtime", tempAuction1.getEnd_time());
            auctionJson1.addProperty("timeleft", tempAuction1.getTime_left());

            auctionArray.add(auctionJson1);

            JsonObject auctionJson2 = new JsonObject();
            auctionJson2.addProperty("domain", tempAuction1.getDomain());
            auctionJson2.addProperty("auction_id", tempAuction1.getAuction_id());
            auctionJson2.addProperty("age", tempAuction1.getAge());
            auctionJson2.addProperty("est", tempAuction1.getEst());
            auctionJson2.addProperty("gdv", tempAuction1.getGdv());
            auctionJson2.addProperty("extns", tempAuction1.getExtns());
            auctionJson2.addProperty("lsv", tempAuction1.getLsv());
            auctionJson2.addProperty("cpc", tempAuction1.getCpc());
            auctionJson2.addProperty("bidders", tempAuction1.getBidders());
            auctionJson2.addProperty("platform", tempAuction1.getPlatform());
            auctionJson2.addProperty("endtime", tempAuction1.getEnd_time());
            auctionJson2.addProperty("timeleft", tempAuction1.getTime_left());
            auctionArray.add(auctionJson2);

            JsonObject response = new JsonObject();
            response.add("data", auctionArray);

            sendMessageToUser(userId, response.toString());
        }
        else if(jsonObject.get("query").toString().contains("getBiddingList")){

// Mock Auction data (creating two example auctions for the list)
            Auction tempAuction1 = new Auction(
                    "auction123", 1000, "2025-03-06T12:00:00", 5, 10, 25, 500,
                    "example.com", 1, "2025-03-07T12:00:00", System.currentTimeMillis(),
                    "Appraisal data", 5000, 2, "auction123-id", "1000", 1, "namecheap",
                    "1 day left", "utf-example", "live", 100, "Appraisal info", 3, 1, 2, 0, 1
            );

            Auction tempAuction2 = new Auction(
                    "auction456", 2000, "2025-03-08T12:00:00", 3, 8, 15, 700,
                    "example2.com", 2, "2025-03-09T12:00:00", System.currentTimeMillis(),
                    "Appraisal data 2", 6000, 3, "auction456-id", "2000", 2, "dropcatch",
                    "2 days left", "utf-example2", "live", 150, "Appraisal info 2", 4, 2, 1, 0, 1
            );

            JsonArray auctionArray = new JsonArray();

            JsonObject auctionJson1 = new JsonObject();
            auctionJson1.addProperty("domain", tempAuction1.getDomain());
            auctionJson1.addProperty("auction_id", tempAuction1.getAuction_id());
            auctionJson1.addProperty("age", tempAuction1.getAge());
            auctionJson1.addProperty("est", tempAuction1.getEst());
            auctionJson1.addProperty("gdv", tempAuction1.getGdv());
            auctionJson1.addProperty("extns", tempAuction1.getExtns());
            auctionJson1.addProperty("lsv", tempAuction1.getLsv());
            auctionJson1.addProperty("cpc", tempAuction1.getCpc());
            auctionJson1.addProperty("bidders", tempAuction1.getBidders());
            auctionJson1.addProperty("platform", tempAuction1.getPlatform());
            auctionJson1.addProperty("endtime", tempAuction1.getEnd_time());
            auctionJson1.addProperty("timeleft", tempAuction1.getTime_left());

            auctionArray.add(auctionJson1);

            JsonObject auctionJson2 = new JsonObject();
            auctionJson2.addProperty("domain", tempAuction1.getDomain());
            auctionJson2.addProperty("auction_id", tempAuction1.getAuction_id());
            auctionJson2.addProperty("age", tempAuction1.getAge());
            auctionJson2.addProperty("est", tempAuction1.getEst());
            auctionJson2.addProperty("gdv", tempAuction1.getGdv());
            auctionJson2.addProperty("extns", tempAuction1.getExtns());
            auctionJson2.addProperty("lsv", tempAuction1.getLsv());
            auctionJson2.addProperty("cpc", tempAuction1.getCpc());
            auctionJson2.addProperty("bidders", tempAuction1.getBidders());
            auctionJson2.addProperty("platform", tempAuction1.getPlatform());
            auctionJson2.addProperty("endtime", tempAuction1.getEnd_time());
            auctionJson2.addProperty("timeleft", tempAuction1.getTime_left());

            auctionArray.add(auctionJson2);

            JsonObject response = new JsonObject();
            response.add("data", auctionArray);

            sendMessageToUser(userId, response.toString());
        }
        else if(jsonObject.get("query").toString().contains("firebase-subsubcollections")){
            JsonObject responseObject=new JsonObject();
            String path=jsonObject.get("path").getAsString();
            responseObject.addProperty("response",ProgramHelper.getSubCollectionsForPath(path).toString());
            sendMessageToUser(userId,responseObject.toString());
        }
        else if(jsonObject.get("query").toString().contains("firebase-allsubsubcollections")){
        JsonObject responseObject=new JsonObject();
        String path=jsonObject.get("path").getAsString();
            Gson gson = new Gson();

            JsonElement jsonElement = gson.toJsonTree(ProgramHelper.getSubcollectionsMap(path));

            responseObject.add("response",jsonElement);
        sendMessageToUser(userId,responseObject.toString());
    }
        else if(jsonObject.get("query").toString().contains("database-sync")){
            JsonObject responseObject=new JsonObject();
            String path=jsonObject.get("path").getAsString();
            responseObject.addProperty("response",""+ProgramHelper.getLatestId(path));
            sendMessageToUser(userId,responseObject.toString());
        }else if(jsonObject.get("query").toString().contains("reconnection-check")){
            JsonObject responseObject=new JsonObject();
            responseObject.addProperty("response","reconnected");
            sendMessageToUser(userId,responseObject.toString());
        }

    }
    // Helper method to parse collections from the incoming JSON
    private Map<String, String> parseCollections(JsonObject checkUpdateData) {
        Map<String, String> collections = new HashMap<>();

        for (String key : checkUpdateData.keySet()) {
            collections.put(key, checkUpdateData.get(key).getAsString());
        }

        return collections;
    }

    // Filter out entries that contain "new" in their value
    private Map<String, String> filterOutNewEntries(Map<String, String> collections) {
        Map<String, String> filteredCollections = new HashMap<>();

        for (Map.Entry<String, String> entry : collections.entrySet()) {
            String value = entry.getValue();
            if (!value.contains("new")) {
                filteredCollections.put(entry.getKey(), value);
            } else {
                System.out.println("Skipping collection " + entry.getKey() + " because it contains 'new': " + value);
            }
        }

        return filteredCollections;
    }

    private String getUserIdFromSession(WebSocketSession session) {
        // Retrieve the userId from the session's attributes
        String userId = (String) session.getAttributes().get("userId");
        return userId != null ? userId : "UnknownUser";
    }

    public void sendMessageToUser(String userId, String message) {

        WebSocketSession userSession = userSessions.get(userId);
        if (userSession != null && userSession.isOpen()) {
            try {
                JsonObject jsonMessage = new JsonObject();
                jsonMessage.addProperty("type", "user");
                jsonMessage.addProperty("data", message);

                userSession.sendMessage(new TextMessage(jsonMessage.toString()));
                System.out.println("Sent user-specific message to user " + userId + " (sessionId: " + userSession.getId() + "): " + message);
            } catch (Exception e) {
                System.err.println("Error sending message to user " + userId + " (sessionId: " + userSession.getId() + "): " + e.getMessage());
            }
        }
    }

    public void broadcastMessage(String type,String path, Map<String, Object> data) {
        final Gson gson = new Gson();

        JsonObject jsonMessage = new JsonObject();
        String jsonData = gson.toJson(data);
        jsonMessage.addProperty("type",type);
        jsonMessage.addProperty("path",path);
        jsonMessage.addProperty("data", jsonData);

        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    String userId = getUserIdFromSession(session);

                    // Check if the user has a mismatched collection for this platform
                    Set<String> mismatchedCollections = mismatchedCollectionsByUser.getOrDefault(userId, new HashSet<>());
                    session.sendMessage(new TextMessage(jsonMessage.toString()));
                    System.out.println("Sent broadcast message to user " + userId + " (sessionId: " + session.getId() + "): " + jsonMessage);
                }
            } catch (Exception e) {
                System.err.println("Error sending message to session " + session.getId() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String userId = getUserIdFromSession(session);
        userSessions.values().removeIf(value -> value.equals(session));
        sessions.remove(session);
        mismatchedCollectionsByUser.remove(userId); // Remove mismatched collections for the user
        System.out.println("WebSocket connection closed for user " + userId + " (sessionId: " + session.getId() + "), Status: " + closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
