package com.tcl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "underlayService",
        "overlayService"
})
public class Product {

    @JsonProperty("underlayService")
    private String underlayService;

    @JsonProperty("overlayService")
    private String overlayService;

    @JsonProperty("underlayService")
    public String getUnderlayService() {
        return underlayService;
    }

    @JsonProperty("underlayService")
    public void setUnderlayService(String underlayService) {
        this.underlayService = underlayService;
    }

    @JsonProperty("overlayService")
    public String getOverlayService() {
        return overlayService;
    }

    @JsonProperty("overlayService")
    public void setOverlayService(String overlayService) {
        this.overlayService = overlayService;
    }

    @Override
    public String toString() {
        return "Product{" +
                "underlayService='" + underlayService + '\'' +
                ", overlayService='" + overlayService + '\'' +
                '}';
    }
}
