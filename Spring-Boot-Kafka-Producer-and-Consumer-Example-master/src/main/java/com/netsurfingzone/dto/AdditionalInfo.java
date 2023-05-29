package main.java.com.netsurfingzone.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class AdditionalInfo {

    public AdditionalInfo(){}

    private String ticketRef;

    private String eventDescription;

    private String bsName;

    private String circle;

    private String city;

    private String bsType;

    private String ip;

    private String siteID;

    private String infraProvider;

    private String iorID;

    private String bsoCktID;

    private String outageStartTime;

    private String outageResolvedTime;

    private String impactedCustomer;

    private String sia;

    public void setTicketRef(String ticketRef){
        this.ticketRef = ticketRef;
    }
    public String getTicketRef(){
        return this.ticketRef;
    }
    public void setEventDescription(String eventDescription){
        this.eventDescription = eventDescription;
    }
    public String getEventDescription(){
        return this.eventDescription;
    }
    public void setBsName(String bsName){
        this.bsName = bsName;
    }
    public String getBsName(){
        return this.bsName;
    }
    public void setCircle(String circle){
        this.circle = circle;
    }
    public String getCircle(){
        return this.circle;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setBsType(String bsType){
        this.bsType = bsType;
    }
    public String getBsType(){
        return this.bsType;
    }
    public void setIp(String ip){
        this.ip = ip;
    }
    public String getIp(){
        return this.ip;
    }
    public void setSiteID(String siteID){
        this.siteID = siteID;
    }
    public String getSiteID(){
        return this.siteID;
    }
    public void setInfraProvider(String infraProvider){
        this.infraProvider = infraProvider;
    }
    public String getInfraProvider(){
        return this.infraProvider;
    }
    public void setIorID(String iorID){
        this.iorID = iorID;
    }
    public String getIorID(){
        return this.iorID;
    }
    public void setBsoCktID(String bsoCktID){
        this.bsoCktID = bsoCktID;
    }
    public String getBsoCktID(){
        return this.bsoCktID;
    }
    public void setOutageStartTime(String outageStartTime){
        this.outageStartTime = outageStartTime;
    }
    public String getOutageStartTime(){
        return this.outageStartTime;
    }
    public void setOutageResolvedTime(String outageResolvedTime){
        this.outageResolvedTime = outageResolvedTime;
    }
    public String getOutageResolvedTime(){
        return this.outageResolvedTime;
    }
    public void setImpactedCustomer(String impactedCustomer){
        this.impactedCustomer = impactedCustomer;
    }
    public String getImpactedCustomer(){
        return this.impactedCustomer;
    }
    public void setSia(String sia){
        this.sia = sia;
    }
    public String getSia(){
        return this.sia;
    }


}
