package com.namekartapp.namekartappapi.godaddy.list;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auctions/godaddy/all")
public class GodaddyListController {


    @PostMapping
    public ResponseEntity<List<AuctionList>> addAuction(@RequestBody List<AuctionList> auctions) {
        String message = new Gson().toJson(auctions);

//        ProgramHelper.doAllSendingAndDatabaseAddingTask("godaddylist","live_list","list",message);





        return ResponseEntity.status(HttpStatus.CREATED).body(auctions);

}

}
