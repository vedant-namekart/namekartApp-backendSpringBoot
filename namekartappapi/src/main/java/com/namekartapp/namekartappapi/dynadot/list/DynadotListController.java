package com.namekartapp.namekartappapi.dynadot.list;

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

import java.util.List;

@RestController
@RequestMapping("/auctions/dynadot/all")
public class DynadotListController {

    @PostMapping
    public ResponseEntity<List<AuctionList>> addAuction(@RequestBody List<AuctionList> auctions) {
        String message = new Gson().toJson(auctions);

//        ProgramHelper.doAllSendingAndDatabaseAddingTask("dynadotlist","live_list","list",message);

        return ResponseEntity.status(HttpStatus.CREATED).body(auctions);
    }
}
