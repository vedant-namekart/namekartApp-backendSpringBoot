package com.namekartapp.namekartappapi.auctions.bulkfetch;

import com.namekartapp.namekartappapi.Auction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auctions/bulkfetch")
public class BulkFetchController {

    // Sample list of auctions for testing
    private List<Auction> mockAuctions;

    // Constructor to initialize sample data
    public BulkFetchController() {
        mockAuctions = new ArrayList<>();

        // Add some mock data for testing
        mockAuctions.add(new Auction("auction1", 100, "2024-12-05", 10, 5, 3, 50, "example1.com", 1, "2024-12-10",
                1672638567000L, "High", 3000, 2, "id1", "2024-11-01", 1, "GoDaddy", "2h", "utf1", "expired", 150, "Good",
                1, 50, 100, 30, 40));
        mockAuctions.add(new Auction("auction2", 200, "2024-11-01", 20, 10, 6, 100, "example2.com", 1, "2024-12-15",
                1672724800000L, "Medium", 5000, 1, "id2", "2024-11-01", 1, "Namecheap", "3h", "utf2", "active", 100, "Average",
                2, 60, 120, 35, 45));
        mockAuctions.add(new Auction("auction3", 150, "2024-12-03", 15, 8, 4, 75, "example3.com", 1, "2024-12-08",
                1672638767000L, "Low", 4000, 3, "id3", "2024-10-20", 1, "Dynadot", "4h", "utf3", "expired", 200, "Excellent",
                3, 55, 110, 32, 42));
    }

    // POST endpoint to fetch auctions based on multiple search queries
    @PostMapping
    public ResponseEntity<?> addAuction(@RequestBody List<SearchQuery> searchQueries) {
        List<Auction> matchingAuctions = new ArrayList<>();

        // Iterate through each search query
        for (SearchQuery query : searchQueries) {
            String platform = query.getPlatform();
            String domain = query.getDomain();

            // Filter auctions based on platform and domain
            List<Auction> filteredAuctions = mockAuctions.stream()
                    .filter(auction -> auction.getPlatform().equalsIgnoreCase(platform) &&
                            auction.getDomain().contains(domain))
                    .collect(Collectors.toList());

            if (!filteredAuctions.isEmpty()) {
                matchingAuctions.addAll(filteredAuctions);
            }
        }

        if (matchingAuctions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No auctions found matching your search queries.");
        }

        // Return the matching auctions in the response
        return ResponseEntity.status(HttpStatus.OK).body(matchingAuctions);
    }

    public static class SearchQuery {
        private String platform;
        private String domain;

        // Getters and Setters
        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }

}
