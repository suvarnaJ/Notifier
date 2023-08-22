package main.java.com.netsurfingzone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.netsurfingzone.dto.AccDetails;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Ticket Reference - TCL",
        "NIMS ID",
        "Maintenance Type",
        "Activity Status",
        "Execution Owner",
        "Activity Window (IST)",
        "Activity Window (GMT)",
        "Expected Impact",
        "Activity Description",
        "Expected Impact Duration(DD:HH:MM)",
        "Extended Up to Time Window (IST)",
        "Extended Up to Time Window (GMT)",
        "Revised Activity Window (IST)",
        "Revised Activity Window (GMT)",
        "eventDescription",
        "bsName",
        "circle",
        "city",
        "bsType",
        "ip",
        "siteID",
        "infraProvider",
        "iorID",
        "bsoCktID",
        "outageStartTime",
        "outageResolvedTime",
        "impactedCustomer",
        "sia",
        "AccDetails"
})
public class AdditionalInfo {

    public AdditionalInfo(){}

    @JsonProperty("Ticket Reference - TCL")
    private String ticketRef;

    @JsonProperty("NIMS ID")
    private String nimsId;

    @JsonProperty("Maintenance Type")
    private String maintenanceType;

    @JsonProperty("Activity Status")
    private String activityStatus;

    @JsonProperty("Execution Owner")
    private String executionOwner;

    @JsonProperty("Activity Window (IST)")
    private String activityWindowIST;

    @JsonProperty("Activity Window (GMT)")
    private String activityWindowGMT;

    @JsonProperty("Expected Impact")
    private String expectedImpact;

    @JsonProperty("Activity Description")
    private String activityDescription;

    @JsonProperty("Expected Impact Duration(DD:HH:MM)")
    private String expectedImpactDurationDD_HH_MM;

    @JsonProperty("Extended Up to Time Window (IST)")
    private String extendedUpToTimeWindowIST;

    @JsonProperty("Extended Up to Time Window (GMT)")
    private String extendedUpToTimeWindowGMT;

    @JsonProperty("Revised Activity Window (IST)")
    private String revisedActivityWindowIST;

    @JsonProperty("Revised Activity Window (GMT)")
    private String revisedActivityWindowGMT;

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

    @JsonProperty("AccDetails")
    private List<AccDetails> accDetails;

    @JsonProperty("Ticket Reference - TCL")
    public void setTicketRef(String ticketRef){
        this.ticketRef = ticketRef;
    }

    @JsonProperty("Ticket Reference - TCL")
    public String getTicketRef(){
        return this.ticketRef;
    }

    @JsonProperty("NIMS ID")
    public String getNimsId() {
        return nimsId;
    }

    @JsonProperty("NIMS ID")
    public void setNimsId(String nimsId) {
        this.nimsId = nimsId;
    }

    @JsonProperty("Maintenance Type")
    public String getMaintenanceType() {
        return maintenanceType;
    }

    @JsonProperty("Maintenance Type")
    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    @JsonProperty("Activity Status")
    public String getActivityStatus() {
        return activityStatus;
    }

    @JsonProperty("Activity Status")
    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    @JsonProperty("Execution Owner")
    public String getExecutionOwner() {
        return executionOwner;
    }

    @JsonProperty("Execution Owner")
    public void setExecutionOwner(String executionOwner) {
        this.executionOwner = executionOwner;
    }

    @JsonProperty("Activity Window (IST)")
    public String getActivityWindowIST() {
        return activityWindowIST;
    }

    @JsonProperty("Activity Window (IST)")
    public void setActivityWindowIST(String activityWindowIST) {
        this.activityWindowIST = activityWindowIST;
    }

    @JsonProperty("Activity Window (GMT)")
    public String getActivityWindowGMT() {
        return activityWindowGMT;
    }

    @JsonProperty("Activity Window (GMT)")
    public void setActivityWindowGMT(String activityWindowGMT) {
        this.activityWindowGMT = activityWindowGMT;
    }

    @JsonProperty("Expected Impact")
    public String getExpectedImpact() {
        return expectedImpact;
    }

    @JsonProperty("Expected Impact")
    public void setExpectedImpact(String expectedImpact) {
        this.expectedImpact = expectedImpact;
    }

    @JsonProperty("Activity Description")
    public String getActivityDescription() {
        return activityDescription;
    }

    @JsonProperty("Activity Description")
    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    @JsonProperty("Expected Impact Duration(DD:HH:MM)")
    public String getExpectedImpactDurationDD_HH_MM() {
        return expectedImpactDurationDD_HH_MM;
    }

    @JsonProperty("Expected Impact Duration(DD:HH:MM)")
    public void setExpectedImpactDurationDD_HH_MM(String expectedImpactDurationDD_HH_MM) {
        this.expectedImpactDurationDD_HH_MM = expectedImpactDurationDD_HH_MM;
    }

    @JsonProperty("Extended Up to Time Window (IST)")
    public String getExtendedUpToTimeWindowIST() {
        return extendedUpToTimeWindowIST;
    }

    @JsonProperty("Extended Up to Time Window (IST)")
    public void setExtendedUpToTimeWindowIST(String extendedUpToTimeWindowIST) {
        this.extendedUpToTimeWindowIST = extendedUpToTimeWindowIST;
    }

    @JsonProperty("Extended Up to Time Window (GMT)")
    public String getExtendedUpToTimeWindowGMT() {
        return extendedUpToTimeWindowGMT;
    }

    @JsonProperty("Extended Up to Time Window (GMT)")
    public void setExtendedUpToTimeWindowGMT(String extendedUpToTimeWindowGMT) {
        this.extendedUpToTimeWindowGMT = extendedUpToTimeWindowGMT;
    }

    @JsonProperty("Revised Activity Window (IST)")
    public String getRevisedActivityWindowIST() {
        return revisedActivityWindowIST;
    }

    @JsonProperty("Revised Activity Window (IST)")
    public void setRevisedActivityWindowIST(String revisedActivityWindowIST) {
        this.revisedActivityWindowIST = revisedActivityWindowIST;
    }

    @JsonProperty("Revised Activity Window (GMT)")
    public String getRevisedActivityWindowGMT() {
        return revisedActivityWindowGMT;
    }

    @JsonProperty("Revised Activity Window (GMT)")
    public void setRevisedActivityWindowGMT(String revisedActivityWindowGMT) {
        this.revisedActivityWindowGMT = revisedActivityWindowGMT;
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

    @JsonProperty("AccDetails")
    public List<AccDetails> getAccDetails() {
        return accDetails;
    }

    public void setAccDetails(List<AccDetails> accDetails) {
        this.accDetails = accDetails;
    }

    @Override
    public String toString() {
        return "AdditionalInfo{" +
                "ticketRef='" + ticketRef + '\'' +
                ", nimsId='" + nimsId + '\'' +
                ", maintenanceType='" + maintenanceType + '\'' +
                ", activityStatus='" + activityStatus + '\'' +
                ", executionOwner='" + executionOwner + '\'' +
                ", activityWindowIST='" + activityWindowIST + '\'' +
                ", activityWindowGMT='" + activityWindowGMT + '\'' +
                ", expectedImpact='" + expectedImpact + '\'' +
                ", activityDescription='" + activityDescription + '\'' +
                ", expectedImpactDurationDD_HH_MM='" + expectedImpactDurationDD_HH_MM + '\'' +
                ", extendedUpToTimeWindowIST='" + extendedUpToTimeWindowIST + '\'' +
                ", extendedUpToTimeWindowGMT='" + extendedUpToTimeWindowGMT + '\'' +
                ", revisedActivityWindowIST='" + revisedActivityWindowIST + '\'' +
                ", revisedActivityWindowGMT='" + revisedActivityWindowGMT + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", bsName='" + bsName + '\'' +
                ", circle='" + circle + '\'' +
                ", city='" + city + '\'' +
                ", bsType='" + bsType + '\'' +
                ", ip='" + ip + '\'' +
                ", siteID='" + siteID + '\'' +
                ", infraProvider='" + infraProvider + '\'' +
                ", iorID='" + iorID + '\'' +
                ", bsoCktID='" + bsoCktID + '\'' +
                ", outageStartTime='" + outageStartTime + '\'' +
                ", outageResolvedTime='" + outageResolvedTime + '\'' +
                ", impactedCustomer='" + impactedCustomer + '\'' +
                ", sia='" + sia + '\'' +
                ", accDetails=" + accDetails +
                '}';
    }
}
