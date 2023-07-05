package com.netsurfingzone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "AccountName",
        "Status",
        "Message",
        "API_Name",
        "Created_At"
})
public class ErrorLogs {

    @JsonProperty("AccountName")
    private String AccountName;
    @JsonProperty("Status")
    private String Status;
    @JsonProperty("Message")
    private String Message;
    @JsonProperty("API_Name")
    private String API_Name;
    @JsonProperty("Created_At")
    private String Created_At;

    @JsonProperty("AccountName")
    public String getAccountName() {
        return AccountName;
    }

    @JsonProperty("AccountName")
    public void setAccountName(String AccountName) {
        this.AccountName = AccountName;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    @JsonProperty("Status")
    public void setStatus(String Status) {
        this.Status = Status;
    }

    @JsonProperty("Message")
    public String getMessage() {
        return Message;
    }

    @JsonProperty("Message")
    public void setMessage(String Message) {
        this.Message = Message;
    }

    @JsonProperty("API_Name")
    public String getApiName() {
        return API_Name;
    }

    @JsonProperty("API_Name")
    public void setApiName(String API_Name) {
        this.API_Name = API_Name;
    }

    @JsonProperty("Created_At")
    public String getCreatedAt() {
        return Created_At;
    }

    @JsonProperty("Created_At")
    public void setCreatedAt(String Created_At) {
        this.Created_At = Created_At;
    }
}
