package com.namekartapp.namekartappapi.dynadot;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.namekartapp.namekartappapi.Auction;
import com.namekartapp.namekartappapi.DomainWebSocketHandler;
import com.namekartapp.namekartappapi.ProgramHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auctions/dynadot")
public class DynadotController {
    private static final Gson gson = new Gson();

    @PostMapping
    public ResponseEntity<Auction> addAuction(@RequestBody Auction auction) {
        String message = new Gson().toJson(auction);


//        ProgramHelper.doAllSendingAndDatabaseAddingTask("dynadot","live","auctions",message);

        // Return the response with the auction data
        return ResponseEntity.status(HttpStatus.CREATED).body(auction);
    }


}
