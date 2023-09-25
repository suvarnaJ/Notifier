package com.tcl.dto;

import java.util.HashMap;
import java.util.Map;

public class Mail {

    public String	eventDescription	;
    public String	ticketRef	;
    public String	bsName	;
    public String	circle	;
    public String	city	;
    public String	bsType	;
    public String	ip	;
    public String	siteID	;
    public String	infraProvider	;
    public String	iorID	;
    public String	bsoCktID	;
    public String	outageStartTime	;
    public String	outageResolvedTime	;
    public String	impactedCustomer	;
    public String	sia	;

    public String summaryData;

    Map model = new HashMap();

    public Map getModel() {
        return model;
    }

    public void setModel(Map model) {
        this.model = model;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getTicketRef() {
        return ticketRef;
    }

    public void setTicketRef(String ticketRef) {
        this.ticketRef = ticketRef;
    }

    public String getBsName() {
        return bsName;
    }

    public void setBsName(String bsName) {
        this.bsName = bsName;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBsType() {
        return bsType;
    }

    public void setBsType(String bsType) {
        this.bsType = bsType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getInfraProvider() {
        return infraProvider;
    }

    public void setInfraProvider(String infraProvider) {
        this.infraProvider = infraProvider;
    }

    public String getIorID() {
        return iorID;
    }

    public void setIorID(String iorID) {
        this.iorID = iorID;
    }

    public String getBsoCktID() {
        return bsoCktID;
    }

    public void setBsoCktID(String bsoCktID) {
        this.bsoCktID = bsoCktID;
    }

    public String getOutageStartTime() {
        return outageStartTime;
    }

    public void setOutageStartTime(String outageStartTime) {
        this.outageStartTime = outageStartTime;
    }

    public String getOutageResolvedTime() {
        return outageResolvedTime;
    }

    public void setOutageResolvedTime(String outageResolvedTime) {
        this.outageResolvedTime = outageResolvedTime;
    }

    public String getImpactedCustomer() {
        return impactedCustomer;
    }

    public void setImpactedCustomer(String impactedCustomer) {
        this.impactedCustomer = impactedCustomer;
    }

    public String getSia() {
        return sia;
    }

    public void setSia(String sia) {
        this.sia = sia;
    }

    public String getSummaryData() {
        return summaryData;
    }

    public void setSummaryData(String summaryData) {
        this.summaryData = summaryData;
    }
}
