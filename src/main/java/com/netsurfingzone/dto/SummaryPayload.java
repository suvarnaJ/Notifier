package com.netsurfingzone.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

@JsonPropertyOrder({
        "AccDetails"
})
public class SummaryPayload {

    @JsonProperty("AccDetails")
    private List<AccDetails> accDetailsList;

    @JsonProperty("fileName")
    private String fileName;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("AccDetails")
    public List<AccDetails> getAccDetailsList() {
        return accDetailsList;
    }

    @JsonProperty("AccDetails")
    public void setAccDetailsList(List<AccDetails> accDetailsList) {
        this.accDetailsList = accDetailsList;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "SummaryPayload{" +
                "accDetailsList=" + accDetailsList +
                ", fileName='" + fileName + '\'' +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
