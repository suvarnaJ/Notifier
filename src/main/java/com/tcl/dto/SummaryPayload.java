package com.tcl.dto;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@JsonPropertyOrder({
        "AccDetails"
})
public class SummaryPayload {

    @JsonProperty("AccDetails")
    private List<AccDetails> accDetailsList;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("AccDetails")
    public List<AccDetails> getAccDetailsList() {
        return accDetailsList;
    }

    @JsonProperty("fileData")
    private byte[] fileData;

    @JsonProperty("fileName")
    private String fileName;

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

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
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
                ", additionalProperties=" + additionalProperties +
                ", fileData=" + "************" +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
