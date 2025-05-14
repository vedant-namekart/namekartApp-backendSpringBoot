package com.namekartapp.namekartappapi;

public class Auction {

    private String auction_id;
    private int est;
    private String addtime;
    private int age;
    private int bidders;
    private int bids;
    private int current_bid_price;
    private int max_bid_price;
    private String domain;
    private int end_list;
    private String end_time;
    private long end_time_stamp;
    private String estibot_appraisal;
    private int gdv;
    private int extns;
    private int lsv;
    private int cpc;
    private int eub;
    private int aby;
    private int highlight;
    private String id;
    private String initial_list;
    private int live;
    private String platform;
    private String time_left;
    private String utf_name;
    private String auction_type;
    private int renewal_price;
    private String appraisal;

    // Default Constructor
    public Auction() {
    }

    // Parameterized Constructor
    public Auction(String auction_id, int est, String addtime, int age, int bidders, int bids, int current_bid_price,
                   String domain, int end_list, String end_time, long end_time_stamp, String estibot_appraisal,
                   int gdv, int highlight, String id, String initial_list, int live, String platform,
                   String time_left, String utf_name, String auction_type, int renewal_price, String appraisal,
                   int extns, int lsv, int cpc, int eub, int aby) {
        this.platform = platform != null ? platform : "";
        this.auction_id = auction_id != null ? auction_id : "";
        this.domain = domain != null ? domain : "";
        this.end_time = end_time != null ? end_time : "";
        this.age = age;
        this.est = est;
        this.gdv = gdv;
        this.extns = extns;
        this.lsv = lsv;
        this.cpc = cpc;
        this.eub = eub;
        this.aby = aby;
        this.bidders = bidders;
        this.auction_type = auction_type != null ? auction_type : "";
        this.addtime = addtime != null ? addtime : "";
        this.bids = bids;
        this.current_bid_price = current_bid_price;
        this.end_list = end_list;
        this.end_time_stamp = end_time_stamp;
        this.estibot_appraisal = estibot_appraisal != null ? estibot_appraisal : "";
        this.highlight = highlight;
        this.id = id != null ? id : "";
        this.initial_list = initial_list != null ? initial_list : "";
        this.live = live;
        this.time_left = time_left != null ? time_left : "";
        this.utf_name = utf_name != null ? utf_name : "";
        this.renewal_price = renewal_price;
        this.appraisal = appraisal != null ? appraisal : "";
    }

    // Getters and Setters
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

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBidders() {
        return bidders;
    }

    public void setBidders(int bidders) {
        this.bidders = bidders;
    }

    public int getBids() {
        return bids;
    }

    public void setBids(int bids) {
        this.bids = bids;
    }

    public int getCurrent_bid_price() {
        return current_bid_price;
    }

    public void setCurrent_bid_price(int current_bid_price) {
        this.current_bid_price = current_bid_price;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getEnd_list() {
        return end_list;
    }

    public void setEnd_list(int end_list) {
        this.end_list = end_list;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public long getEnd_time_stamp() {
        return end_time_stamp;
    }

    public void setEnd_time_stamp(long end_time_stamp) {
        this.end_time_stamp = end_time_stamp;
    }

    public String getEstibot_appraisal() {
        return estibot_appraisal;
    }

    public void setEstibot_appraisal(String estibot_appraisal) {
        this.estibot_appraisal = estibot_appraisal;
    }

    public int getGdv() {
        return gdv;
    }

    public void setGdv(int gdv) {
        this.gdv = gdv;
    }

    public int getHighlight() {
        return highlight;
    }

    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInitial_list() {
        return initial_list;
    }

    public void setInitial_list(String initial_list) {
        this.initial_list = initial_list;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTime_left() {
        return time_left;
    }

    public void setTime_left(String time_left) {
        this.time_left = time_left;
    }

    public String getUtf_name() {
        return utf_name;
    }

    public void setUtf_name(String utf_name) {
        this.utf_name = utf_name;
    }

    public String getAuction_type() {
        return auction_type;
    }

    public void setAuction_type(String auction_type) {
        this.auction_type = auction_type;
    }

    public int getRenewal_price() {
        return renewal_price;
    }

    public void setRenewal_price(int renewal_price) {
        this.renewal_price = renewal_price;
    }

    public String getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(String appraisal) {
        this.appraisal = appraisal;
    }

    public int getExtns() {
        return extns;
    }

    public void setExtns(int extns) {
        this.extns = extns;
    }

    public int getLsv() {
        return lsv;
    }

    public void setLsv(int lsv) {
        this.lsv = lsv;
    }

    public int getCpc() {
        return cpc;
    }

    public void setCpc(int cpc) {
        this.cpc = cpc;
    }

    public int getEub() {
        return eub;
    }

    public void setEub(int eub) {
        this.eub = eub;
    }

    public int getAby() {
        return aby;
    }

    public void setAby(int aby) {
        this.aby = aby;
    }

    public int getMax_bid_price() {
        return max_bid_price;
    }

    public void setMax_bid_price(int max_bid_price) {
        this.max_bid_price = max_bid_price;
    }
}
