package com.quicsolv.insurance.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"vendorID",
"vendorWork"
})
public class Vendor {

@JsonProperty("vendorID")
private String vendorID;
@JsonProperty("vendorWork")
private String vendorWork;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("vendorID")
public String getVendorID() {
return vendorID;
}

@JsonProperty("vendorID")
public void setVendorID(String vendorID) {
this.vendorID = vendorID;
}

@JsonProperty("vendorWork")
public String getVendorWork() {
return vendorWork;
}

@JsonProperty("vendorWork")
public void setVendorWork(String vendorWork) {
this.vendorWork = vendorWork;
}



@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}



@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}



}