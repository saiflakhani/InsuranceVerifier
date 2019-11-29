package com.quicsolv.insurance.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Time implements Serializable{

    @SerializedName("caseUploaded")
    @Expose
    private String caseUploaded;
    @SerializedName("assignedToVendor")
    @Expose
    private String assignedToVendor;
    @SerializedName("assignedToVerifier")
    @Expose
    private String assignedToVerifier;
    @SerializedName("submittedByVerifier")
    @Expose
    private Object submittedByVerifier;
    @SerializedName("approvedByVendor")
    @Expose
    private Object approvedByVendor;
    @SerializedName("reassignedByVendor")
    @Expose
    private Object reassignedByVendor;
    @SerializedName("clearedByAdmin")
    @Expose
    private Object clearedByAdmin;
    @SerializedName("rejectedByAdmin")
    @Expose
    private Object rejectedByAdmin;

    public String getCaseUploaded() {
        return caseUploaded;
    }

    public void setCaseUploaded(String caseUploaded) {
        this.caseUploaded = caseUploaded;
    }

    public String getAssignedToVendor() {
        return assignedToVendor;
    }

    public void setAssignedToVendor(String assignedToVendor) {
        this.assignedToVendor = assignedToVendor;
    }

    public String getAssignedToVerifier() {
        return assignedToVerifier;
    }

    public void setAssignedToVerifier(String assignedToVerifier) {
        this.assignedToVerifier = assignedToVerifier;
    }

    public Object getSubmittedByVerifier() {
        return submittedByVerifier;
    }

    public void setSubmittedByVerifier(Object submittedByVerifier) {
        this.submittedByVerifier = submittedByVerifier;
    }

    public Object getApprovedByVendor() {
        return approvedByVendor;
    }

    public void setApprovedByVendor(Object approvedByVendor) {
        this.approvedByVendor = approvedByVendor;
    }

    public Object getReassignedByVendor() {
        return reassignedByVendor;
    }

    public void setReassignedByVendor(Object reassignedByVendor) {
        this.reassignedByVendor = reassignedByVendor;
    }

    public Object getClearedByAdmin() {
        return clearedByAdmin;
    }

    public void setClearedByAdmin(Object clearedByAdmin) {
        this.clearedByAdmin = clearedByAdmin;
    }

    public Object getRejectedByAdmin() {
        return rejectedByAdmin;
    }

    public void setRejectedByAdmin(Object rejectedByAdmin) {
        this.rejectedByAdmin = rejectedByAdmin;
    }

}