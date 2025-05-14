package com.namekartapp.namekartappapi.namecheap;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.namekartapp.namekartappapi.Auction;
import com.namekartapp.namekartappapi.ProgramHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auctions/namecheap")
public class NamecheapController {

    @PostMapping
    public ResponseEntity<Auction> addAuction(@RequestBody Auction auction) {
        String message = new Gson().toJson(auction);

//        ProgramHelper.doAllSendingAndDatabaseAddingTask("namecheap","live","auctions",message);

        return ResponseEntity.status(HttpStatus.CREATED).body(auction);
    }
}
