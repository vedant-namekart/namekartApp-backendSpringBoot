package com.namekartapp.namekartappapi.auctions.bulkbid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auctions/bulkbid/instant")
public class BulkBidInstantController {
    // POST endpoint to add a new auction
    @PostMapping
    public ResponseEntity<?> addAuction(@RequestBody String[] auction) {
        for(String a:auction){
            System.out.println(a);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Processed instant bid");
    }
}

