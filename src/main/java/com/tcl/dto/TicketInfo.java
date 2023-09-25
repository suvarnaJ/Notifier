package main.java.com.tcl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketInfo {

    private String number;

    @JsonProperty("serviceId")
    private String serviceId;

    @JsonProperty("date")
    private String date;

    private String circuitId;
    private String asset;
    private String productName;
    private String tableName;
    private String businessUnit;
    private String operation;
    private String impact;
    private String customerReferenceNumber;
    private String account;
    private String serviceDowntimeStart;
    private String priority;
    private String shortDescription;
    private String description;
    private String statusReason;
    private String parent;
    private String state;
    private String rfoSpecification;
    private String closeNotes;
    private String closedBy;
    private String rfoCause;
    private String problemSiteAddress;
    private String parentAccount;
    private String createdBy;
    private String issueDescription;
    private String assignedTo;
    private String assignmentGroup;
    private String dueDate;
    private String requestItemVariablesDisputeId;
    private String createdOn;
    private String catItem;
    private String closedAt;
    private String pendingWith;
    private String jiraId;
    private String entity;
    private String type;
    private String contactType;
    private String startDate;
    private String endDate;

    @JsonProperty("location")
    private String location;

    private String requestedBy;
    private String category;
    private String subCategory;
    private String sysUpdatedOn;
    private String riskImpactAnalysis;
    private String cmdbCiAssetModel;
    private String closeCode;
    private String surveyLink;
    private String protectionState;


    // Getter Methods

    public String getNumber() {
        return number;
    }

    @JsonProperty("serviceId")
    public String getServiceId() {
        return serviceId;
    }

    public String getAsset() {
        return asset;
    }

    public String getProductName() {
        return productName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public String getOperation() {
        return operation;
    }

    public String getImpact() {
        return impact;
    }

    public String getCustomerReferenceNumber() {
        return customerReferenceNumber;
    }

    public String getAccount() {
        return account;
    }

    public String getServiceDowntimeStart() {
        return serviceDowntimeStart;
    }

    public String getPriority() {
        return priority;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public String getParent() {
        return parent;
    }

    public String getState() {
        return state;
    }

    public String getCloseNotes() {
        return closeNotes;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public String getRfoCause() {
        return rfoCause;
    }

    public String getProblemSiteAddress() {
        return problemSiteAddress;
    }

    public String getParentAccount() {
        return parentAccount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public String getAssignmentGroup() {
        return assignmentGroup;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getRequestItemVariablesDisputeId() {
        return requestItemVariablesDisputeId;
    }

    public String getCatItem() {
        return catItem;
    }

    public String getClosedAt() {
        return closedAt;
    }

    public String getPendingWith() {
        return pendingWith;
    }

    public String getJiraId() {
        return jiraId;
    }

    public String getEntity() {
        return entity;
    }

    public String getType() {
        return type;
    }

    public String getContactType() {
        return contactType;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getSysUpdatedOn() {
        return sysUpdatedOn;
    }

    public String getRiskImpactAnalysis() {
        return riskImpactAnalysis;
    }

    public String getCmdbCiAssetModel() {
        return cmdbCiAssetModel;
    }

    public String getCloseCode() {
        return closeCode;
    }

    public String getSurveyLink() {
        return surveyLink;
    }

    public String getProtectionState() {
        return protectionState;
    }

    // Setter Methods

    public void setNumber(String number) {
        this.number = number;
    }

    @JsonProperty("serviceId")
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public void setCustomerReferenceNumber(String customerReferenceNumber) {
        this.customerReferenceNumber = customerReferenceNumber;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setServiceDowntimeStart(String serviceDowntimeStart) {
        this.serviceDowntimeStart = serviceDowntimeStart;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCloseNotes(String closeNotes) {
        this.closeNotes = closeNotes;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public void setRfoCause(String rfoCause) {
        this.rfoCause = rfoCause;
    }

    public void setProblemSiteAddress(String problemSiteAddress) {
        this.problemSiteAddress = problemSiteAddress;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setAssignmentGroup(String assignmentGroup) {
        this.assignmentGroup = assignmentGroup;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setRequestItemVariablesDisputeId(String requestItemVariablesDisputeId) {
        this.requestItemVariablesDisputeId = requestItemVariablesDisputeId;
    }

    public void setCatItem(String catItem) {
        this.catItem = catItem;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    public void setPendingWith(String pendingWith) {
        this.pendingWith = pendingWith;
    }

    public void setJiraId(String jiraId) {
        this.jiraId = jiraId;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setSysUpdatedOn(String sysUpdatedOn) {
        this.sysUpdatedOn = sysUpdatedOn;
    }

    public void setRiskImpactAnalysis(String riskImpactAnalysis) {
        this.riskImpactAnalysis = riskImpactAnalysis;
    }

    public void setCmdbCiAssetModel(String cmdbCiAssetModel) {
        this.cmdbCiAssetModel = cmdbCiAssetModel;
    }

    public void setCloseCode(String closeCode) {
        this.closeCode = closeCode;
    }

    public void setSurveyLink(String surveyLink) {
        this.surveyLink = surveyLink;
    }

    public void setProtectionState(String protectionState) {
        this.protectionState = protectionState;
    }

    public String getRfoSpecification() {
        return rfoSpecification;
    }

    public void setRfoSpecification(String rfoSpecification) {
        this.rfoSpecification = rfoSpecification;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }
}
