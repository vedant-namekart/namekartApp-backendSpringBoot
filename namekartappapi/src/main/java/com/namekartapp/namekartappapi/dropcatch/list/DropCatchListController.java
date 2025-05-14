package com.namekartapp.namekartappapi.dropcatch.list;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.namekartapp.namekartappapi.AuctionList;
import com.namekartapp.namekartappapi.ProgramHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auctions/dropcatch/all")
public class DropCatchListController {

    // POST endpoint to add a list of auctions
    @PostMapping
    public ResponseEntity<List<AuctionList>> addAuctions(@RequestBody List<AuctionList> auctions) {
        String message = new Gson().toJson(auctions);

//        ProgramHelper.doAllSendingAndDatabaseAddingTask("dropcatchlist","live_list","list",message);


        // Respond with the added auctions
        return ResponseEntity.status(HttpStatus.CREATED).body(auctions);
    }

}
