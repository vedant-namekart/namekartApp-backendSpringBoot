package com.namekartapp.namekartappapi.godaddy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.namekartapp.namekartappapi.Auction;
import com.namekartapp.namekartappapi.FCMService;
import com.namekartapp.namekartappapi.ProgramHelper;
import jakarta.servlet.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auctions/godaddy")
public class GodaddyController {
    private static final Gson gson = new Gson();

    @Autowired
    private FCMService fcmService;

    @PostMapping
    public Map<String, Object> addAuction(@RequestBody Map<String, Object> data) {
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            Auction auction = objectMapper.convertValue(((List<Map<String, Object>>) data.get("data")).get(0), Auction.class);
            System.out.println(auction.getAuction_id());

//            ProgramHelper.doAllSendingAndDatabaseAddingTask("godaddy", "live", "auctions", data);

            // Send FCM notification to the "auctions" topic
            fcmService.sendNotificationToTopic(
                    "auctions", // Topic name
                    "New Auction Added",
                    "Auction: " + auction.getDomain(),
                    data
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(data).getBody();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return (Map<String, Object>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
