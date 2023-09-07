package main.java.com.netsurfingzone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.netsurfingzone.dto.AccDetails;
import com.netsurfingzone.dto.Product;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "nimsId",
        "activityStatus",
        "revisedDateIST",
        "expectedImpact",
        "timeWindowIST",
        "revisedEndDate",
        "revisedActivityWindowIST",
        "revisedActivityWindowGMT",
        "reminder",
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

    @JsonProperty("nimsId")
    private String nimsId;

    @JsonProperty("revisedDateIST")
    private String activityWindowIST;

    @JsonProperty("expectedImpact")
    private String expectedImpact;

    @JsonProperty("timeWindowIST")
    private String extendedUpToTimeWindowIST;

    @JsonProperty("revisedEndDate")
    private String extendedUpToTimeWindowGMT;

    @JsonProperty("revisedActivityWindowIST")
    private String revisedActivityWindowIST;

    @JsonProperty("revisedActivityWindowGMT")
    private String revisedActivityWindowGMT;

    @JsonProperty("reminder")
    private String reminder;

    @JsonProperty("Product")
    private List<com.netsurfingzone.dto.Product> Product;

    private String siteIsolationOrServiceDegradation;

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

    @JsonProperty("nimsId")
    public String getNimsId() {
        return nimsId;
    }

    @JsonProperty("nimsId")
    public void setNimsId(String nimsId) {
        this.nimsId = nimsId;
    }

    @JsonProperty("revisedDateIST")
    public String getActivityWindowIST() {
        return activityWindowIST;
    }

    @JsonProperty("revisedDateIST")
    public void setActivityWindowIST(String activityWindowIST) {
        this.activityWindowIST = activityWindowIST;
    }

    @JsonProperty("expectedImpact")
    public String getExpectedImpact() {
        return expectedImpact;
    }

    @JsonProperty("expectedImpact")
    public void setExpectedImpact(String expectedImpact) {
        this.expectedImpact = expectedImpact;
    }

    @JsonProperty("extendedUpToTimeWindowIST")
    public String getExtendedUpToTimeWindowIST() {
        return extendedUpToTimeWindowIST;
    }

    @JsonProperty("timeWindowIST")
    public void setExtendedUpToTimeWindowIST(String extendedUpToTimeWindowIST) {
        this.extendedUpToTimeWindowIST = extendedUpToTimeWindowIST;
    }

    @JsonProperty("timeWindowIST")
    public String getExtendedUpToTimeWindowGMT() {
        return extendedUpToTimeWindowGMT;
    }

    @JsonProperty("revisedEndDate")
    public void setExtendedUpToTimeWindowGMT(String extendedUpToTimeWindowGMT) {
        this.extendedUpToTimeWindowGMT = extendedUpToTimeWindowGMT;
    }

    @JsonProperty("revisedEndDate")
    public String getRevisedActivityWindowIST() {
        return revisedActivityWindowIST;
    }

    @JsonProperty("revisedActivityWindowIST")
    public void setRevisedActivityWindowIST(String revisedActivityWindowIST) {
        this.revisedActivityWindowIST = revisedActivityWindowIST;
    }

    @JsonProperty("revisedActivityWindowGMT")
    public String getRevisedActivityWindowGMT() {
        return revisedActivityWindowGMT;
    }

    @JsonProperty("revisedActivityWindowGMT")
    public void setRevisedActivityWindowGMT(String revisedActivityWindowGMT) {
        this.revisedActivityWindowGMT = revisedActivityWindowGMT;
    }

    @JsonProperty("reminder")
    public String getReminder() {
        return reminder;
    }

    @JsonProperty("reminder")
    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    @JsonProperty("Product")
    public List<com.netsurfingzone.dto.Product> getProduct() {
        return Product;
    }

    @JsonProperty("Product")
    public void setProduct(List<com.netsurfingzone.dto.Product> product) {
        Product = product;
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

    public String getSiteIsolationOrServiceDegradation() {
        return siteIsolationOrServiceDegradation;
    }

    public void setSiteIsolationOrServiceDegradation(String siteIsolationOrServiceDegradation) {
        this.siteIsolationOrServiceDegradation = siteIsolationOrServiceDegradation;
    }

    @Override
    public String toString() {
        return "AdditionalInfo{" +
                "nimsId='" + nimsId + '\'' +
                ", activityWindowIST='" + activityWindowIST + '\'' +
                ", expectedImpact='" + expectedImpact + '\'' +
                ", extendedUpToTimeWindowIST='" + extendedUpToTimeWindowIST + '\'' +
                ", extendedUpToTimeWindowGMT='" + extendedUpToTimeWindowGMT + '\'' +
                ", revisedActivityWindowIST='" + revisedActivityWindowIST + '\'' +
                ", revisedActivityWindowGMT='" + revisedActivityWindowGMT + '\'' +
                ", reminder='" + reminder + '\'' +
                ", Product=" + Product +
                ", siteIsolationOrServiceDegradation='" + siteIsolationOrServiceDegradation + '\'' +
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
