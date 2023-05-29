package com.netsurfingzone.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "templateData")
public class templateData {

    public String id;
    public String templatePayload;

    public templateData(){}

    public templateData(String id, String templatePayload) {
        this.id = id;
        this.templatePayload = templatePayload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplatePayload() {
        return templatePayload;
    }

    public void setTemplatePayload(String templatePayload) {
        this.templatePayload = templatePayload;
    }
}
