package com.namekartapp.namekartappapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auctions")
public class DomainController {
    private final List<Auction> auctionList = new ArrayList<>();

    // Initialize with some default auctions if necessary
    public DomainController() {
    }

    // Get all auctions
    @GetMapping
    public List<Auction> getAuctions() {
        return auctionList;
    }

    // POST endpoint to add a new auction
    @PostMapping
    public ResponseEntity<Auction> addAuction(@RequestBody Auction auction) {
        auctionList.add(auction);
        System.out.println("Added auction: " + auction.getDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(auction);
    }
}
