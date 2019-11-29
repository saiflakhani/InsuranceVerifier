package com.quicsolv.insurance.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ApplicantDataVO implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("invResults")
    @Expose
    private String invResults;
    @SerializedName("applicantName")
    @Expose
    private String applicantName;
    @SerializedName("policyNo")
    @Expose
    private String policyNo;
    @SerializedName("contact")
    @Expose
    private String contact;
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
    @SerializedName("invCategory")
    @Expose
    private String invCategory;
    @SerializedName("invType")
    @Expose
    private String invType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("documentType")
    @Expose
    private String documentType;
    @SerializedName("uniqueID")
    @Expose
    private String uniqueID;
    @SerializedName("receiptDate")
    @Expose
    private String receiptDate;
    @SerializedName("time")
    @Expose
    private Time time;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("photoList")
    @Expose
    private List<PhotoList> photoList = null;
    @SerializedName("vendors")
    @Expose
    private List<Vendor> vendors = null;
    @SerializedName("verifierAssigned")
    @Expose
    private VerifierAssigned verifierAssigned;

    private List<QuestionDataVO> questionsAsked = new ArrayList<>();

    public List<QuestionDataVO> getQuestionsAsked() {
        return questionsAsked;
    }

    public void setQuestionsAsked(List<QuestionDataVO> questionsAsked) {
        this.questionsAsked = questionsAsked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvResults() {
        return invResults;
    }

    public void setInvResults(String invResults) {
        this.invResults = invResults;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<PhotoList> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<PhotoList> photoList) {
        this.photoList = photoList;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    public VerifierAssigned getVerifierAssigned() {
        return verifierAssigned;
    }

    public void setVerifierAssigned(VerifierAssigned verifierAssigned) {
        this.verifierAssigned = verifierAssigned;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}