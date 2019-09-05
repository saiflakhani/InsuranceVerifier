package com.quicsolv.insurance.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ApplicantDataVO implements Serializable {

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    @SerializedName("uniqueID")
    @Expose
    private String uniqueID;

    @SerializedName("applicantName")
    @Expose
    private String applicantName;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("pinCode")
    @Expose
    private String pinCode;
    @SerializedName("watchCategory")
    @Expose
    private String watchCategory;
    @SerializedName("referencedBy")
    @Expose
    private String referencedBy;
    @SerializedName("receiptDate")
    @Expose
    private String receiptDate;
    @SerializedName("documentType")
    @Expose
    private String documentType;
    @SerializedName("invCategory")
    @Expose
    private String invCategory;
    @SerializedName("invType")
    @Expose
    private String invType;
    @SerializedName("invResults")
    @Expose
    private String invResults;
    @SerializedName("status")
    @Expose
    private String status;

    public List<QuestionDataVO> getQuestionsAsked() {
        return questionsAsked;
    }

    @JsonProperty("vendors")
    private List<Vendor> vendors = null;

    @JsonProperty("vendors")
    public List<Vendor> getVendors() {
        return vendors;
    }

    @JsonProperty("vendors")
    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }


    public List<PhotoVO> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<PhotoVO> photoList) {
        this.photoList = photoList;
    }

    public List<PhotoVO> photoList;

    public void setQuestionsAsked(List<QuestionDataVO> questionsAsked) {
        this.questionsAsked = questionsAsked;
    }

    private List<QuestionDataVO> questionsAsked = new ArrayList<>();

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getWatchCategory() {
        return watchCategory;
    }

    public void setWatchCategory(String watchCategory) {
        this.watchCategory = watchCategory;
    }

    public String getReferencedBy() {
        return referencedBy;
    }

    public void setReferencedBy(String referencedBy) {
        this.referencedBy = referencedBy;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getInvCategory() {
        return invCategory;
    }

    public void setInvCategory(String invCategory) {
        this.invCategory = invCategory;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
    }

    public String getInvResults() {
        return invResults;
    }

    public void setInvResults(String invResults) {
        this.invResults = invResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}