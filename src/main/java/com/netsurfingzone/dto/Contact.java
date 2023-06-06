package main.java.com.netsurfingzone.dto;

public class Contact {
    private String to;
    private String cc;
    private String watchList;


    // Getter Methods

    public String getTo() {
        return to;
    }

    public String getCc() {
        return cc;
    }

    public String getWatchList() {
        return watchList;
    }

    // Setter Methods

    public void setTo(String to) {
        this.to = to;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setWatchList(String watchList) {
        this.watchList = watchList;
    }
}
