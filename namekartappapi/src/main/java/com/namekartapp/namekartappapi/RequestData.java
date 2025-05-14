package com.namekartapp.namekartappapi;

public class RequestData {

    private String action;   // Action to be performed (e.g., "place bid", "check update", etc.)
    private String userId;   // The user making the request
    private String auctionId; // The auction involved in the action
    private Double bidAmount; // Bid amount (only for "place bid" action)
    private String datetime;  // DateTime (for "check update" action)

    // Getters and Setters
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public Double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
