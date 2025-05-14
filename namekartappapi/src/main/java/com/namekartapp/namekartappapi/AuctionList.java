package com.namekartapp.namekartappapi;

public class AuctionList {
    private String auction_id;
    private int est;
    private String domain;
    private int age;
    private int gdv;
    private int current_bid_price;

    private String highlightType;

    public AuctionList() {}

    public AuctionList(String auction_id, int est, int age, int current_bid_price, String domain, int gdv,String highlightType) {
        this.auction_id = auction_id != null ? auction_id : "";
        this.domain = domain != null ? domain : "";
        this.est = est;
        this.age = age;
        this.gdv = gdv;
        this.current_bid_price = current_bid_price;
        this.highlightType=highlightType;
    }

    public String getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(String auction_id) {
        this.auction_id = auction_id;
    }

    public int getEst() {
        return est;
    }

    public void setEst(int est) {
        this.est = est;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGdv() {
        return gdv;
    }

    public void setGdv(int gdv) {
        this.gdv = gdv;
    }

    public int getCurrent_bid_price() {
        return current_bid_price;
    }

    public void setCurrent_bid_price(int current_bid_price) {
        this.current_bid_price = current_bid_price;
    }

    public String getHighlightType() {
        return highlightType;
    }

    public void setHighlightType(String highlightType) {
        this.highlightType = highlightType;
    }
}
