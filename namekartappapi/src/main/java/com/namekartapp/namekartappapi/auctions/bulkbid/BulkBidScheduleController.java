package com.namekartapp.namekartappapi.auctions.bulkbid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auctions/bulkbid/schedule")
public class BulkBidScheduleController {
    // POST endpoint to add a new auction
    @PostMapping
    public ResponseEntity<?> addAuction(@RequestBody String[] auction) {
        for(String a:auction){
            System.out.println(a);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Processed schedule bid");
    }
}

