package com.namekartapp.namekartappapi.auctions.BiddingList;

import com.namekartapp.namekartappapi.Auction;
import com.namekartapp.namekartappapi.auctions.watchlist.WatchlistWebSocketHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/auctions/biddinglist")
public class BiddingListController {
    List<Auction> watchList=new ArrayList<>();

    public BiddingListController(){}

    @GetMapping
    public List<Auction> getWatchList(){

        return watchList;
    }

    @PostMapping
    public ResponseEntity<List<Auction>> addWatchList(@RequestBody List<Auction> auctions){
        watchList.addAll(auctions);
        WatchlistWebSocketHandler.sendAuctionUpdate(auctions);
        auctions.forEach(auction->System.out.println("Added Domain: "+auction.getDomain()));
        return ResponseEntity.status(HttpStatus.CREATED).body(auctions);
    }


}

