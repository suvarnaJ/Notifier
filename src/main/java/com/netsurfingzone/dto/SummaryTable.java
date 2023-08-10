package com.netsurfingzone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class SummaryTable {

    private String ticketNumber;
    private String serviceID;
    private String accountName;
    private String bandwidth;
    private String impact;
    private String state;
    private String statusReason;
    private String product;
    private String a_end_site_address;
    private String latest_update;

    Map model = new HashMap();

    public Map getModel() {
        return model;
    }

    public void setModel(Map model) {
        this.model = model;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getA_end_site_address() {
        return a_end_site_address;
    }

    public void setA_end_site_address(String a_end_site_address) {
        this.a_end_site_address = a_end_site_address;
    }

    public String getLatest_update() {
        return latest_update;
    }

    public void setLatest_update(String latest_update) {
        this.latest_update = latest_update;
    }

    @Override
    public String toString() {
        return "SummaryTable{" +
                "ticketNumber='" + ticketNumber + '\'' +
                ", serviceID='" + serviceID + '\'' +
                ", accountName='" + accountName + '\'' +
                ", bandwidth='" + bandwidth + '\'' +
                ", impact='" + impact + '\'' +
                ", state='" + state + '\'' +
                ", statusReason='" + statusReason + '\'' +
                ", product='" + product + '\'' +
                ", a_end_site_address='" + a_end_site_address + '\'' +
                ", latest_update='" + latest_update + '\'' +
                ", model=" + model +
                '}';
    }
}
