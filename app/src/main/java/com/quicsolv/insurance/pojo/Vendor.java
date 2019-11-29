package com.quicsolv.insurance.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"vendorID",
"vendorWork"
})
public class Vendor implements Serializable {

    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("vendorWork")
    @Expose
    private String vendorWork;
    @SerializedName("vendorUserName")
    @Expose
    private String vendorUserName;

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getVendorWork() {
        return vendorWork;
    }

    public void setVendorWork(String vendorWork) {
        this.vendorWork = vendorWork;
    }

    public String getVendorUserName() {
        return vendorUserName;
    }

    public void setVendorUserName(String vendorUserName) {
        this.vendorUserName = vendorUserName;
    }

}