package com.netsurfingzone.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "TicketNumber",
        "ServiceID",
        "Accountname",
        "bandwidth",
        "category",
        "state",
        "StatusReason",
        "to_email",
        "cc_email",
        "opened_at",
        "product",
        "a_end_site_address",
        "latest_update"
})
public class AccDetails {

    @JsonProperty("TicketNumber")
    private String ticketNumber;
    @JsonProperty("ServiceID")
    private String serviceID;
    @JsonProperty("Accountname")
    private String accountname;
    @JsonProperty("bandwidth")
    private String bandwidth;
    @JsonProperty("impact")
    private String impact;
    @JsonProperty("state")
    private String state;
    @JsonProperty("StatusReason")
    private String statusReason;
    @JsonProperty("to_email")
    private String toEmail;
    @JsonProperty("cc_email")
    private String ccEmail;
    @JsonProperty("opened_at")
    private String openedAt;
    @JsonProperty("product")
    private String product;
    @JsonProperty("a_end_site_address")
    private String a_end_site_address;
    @JsonProperty("latest_update")
    private String latest_update;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("TicketNumber")
    public String getTicketNumber() {
        return ticketNumber;
    }

    @JsonProperty("TicketNumber")
    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @JsonProperty("ServiceID")
    public String getServiceID() {
        return serviceID;
    }

    @JsonProperty("ServiceID")
    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    @JsonProperty("Accountname")
    public String getAccountname() {
        return accountname;
    }

    @JsonProperty("Accountname")
    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    @JsonProperty("bandwidth")
    public String getBandwidth() {
        return bandwidth;
    }

    @JsonProperty("bandwidth")
    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    @JsonProperty("impact")
    public String getImpact() {
        return impact;
    }

    @JsonProperty("impact")
    public void setImpact(String impact) {
        this.impact = impact;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("StatusReason")
    public String getStatusReason() {
        return statusReason;
    }

    @JsonProperty("StatusReason")
    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    @JsonProperty("to_email")
    public String getToEmail() {
        return toEmail;
    }

    @JsonProperty("to_email")
    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    @JsonProperty("cc_email")
    public String getCcEmail() {
        return ccEmail;
    }

    @JsonProperty("cc_email")
    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail;
    }

    @JsonProperty("opened_at")
    public String getOpenedAt() {
        return openedAt;
    }

    @JsonProperty("opened_at")
    public void setOpenedAt(String openedAt) {
        this.openedAt = openedAt;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    @JsonProperty("product")
    public String getProduct() {
        return product;
    }

    @JsonProperty("product")
    public void setProduct(String product) {
        this.product = product;
    }

    @JsonProperty("a_end_site_address")
    public String getA_end_site_address() {
        return a_end_site_address;
    }

    @JsonProperty("a_end_site_address")
    public void setA_end_site_address(String a_end_site_address) {
        this.a_end_site_address = a_end_site_address;
    }

    @JsonProperty("latest_update")
    public String getLatest_update() {
        return latest_update;
    }

    @JsonProperty("latest_update")
    public void setLatest_update(String latest_update) {
        this.latest_update = latest_update;
    }

    @Override
    public String toString() {
        return "AccDetails{" +
                "ticketNumber='" + ticketNumber + '\'' +
                ", serviceID='" + serviceID + '\'' +
                ", accountname='" + accountname + '\'' +
                ", bandwidth='" + bandwidth + '\'' +
                ", impact='" + impact + '\'' +
                ", state='" + state + '\'' +
                ", statusReason='" + statusReason + '\'' +
                ", toEmail='" + toEmail + '\'' +
                ", ccEmail='" + ccEmail + '\'' +
                ", openedAt='" + openedAt + '\'' +
                ", product='" + product + '\'' +
                ", a_end_site_address='" + a_end_site_address + '\'' +
                ", latest_update='" + latest_update + '\'' +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
