package com.namekartapp.namekartappapi;

import java.util.HashMap;
import java.util.Map;

public class UserSessionData {

    private final Map<String, Double> placedBids; // Map of auctionId -> bidAmount
    private final Map<String, String> lastCheckedDatetime; // Map of auctionId -> last checked datetime

    public UserSessionData() {
        placedBids = new HashMap<>();
        lastCheckedDatetime = new HashMap<>();
    }

    public void addBid(String auctionId, double bidAmount) {
        placedBids.put(auctionId, bidAmount);
    }

    public boolean removeBid(String auctionId) {
        if (placedBids.containsKey(auctionId)) {
            placedBids.remove(auctionId);
            return true;
        }
        return false;
    }

    public boolean hasPlacedBid(String auctionId) {
        return placedBids.containsKey(auctionId);
    }

    public double getBidAmount(String auctionId) {
        return placedBids.getOrDefault(auctionId, 0.0);
    }

    public void setLastCheckedDatetime(String auctionId, String datetime) {
        lastCheckedDatetime.put(auctionId, datetime);
    }

    public String getLastCheckedDatetime(String auctionId) {
        return lastCheckedDatetime.getOrDefault(auctionId, null);
    }

    public boolean isDataUpdated(String auctionId, String userDatetime) {
        String lastDatetime = getLastCheckedDatetime(auctionId);
        return lastDatetime != null && lastDatetime.equals(userDatetime);
    }

    public Map<String, Double> getPlacedBids() {
        return placedBids;
    }

    public Map<String, String> getLastCheckedDatetime() {
        return lastCheckedDatetime;
    }
}
