package com.quicsolv.insurance.pojo;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_id",
        "verfierName",
        "contact",
        "vendorAffiliation"
})
public class Verifier {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("verfierName")
    private String verfierName;
    @JsonProperty("contact")
    private String contact;
    @JsonProperty("vendorAffiliation")
    private String vendorAffiliation;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("verfierName")
    public String getVerfierName() {
        return verfierName;
    }

    @JsonProperty("verfierName")
    public void setVerfierName(String verfierName) {
        this.verfierName = verfierName;
    }

    @JsonProperty("contact")
    public String getContact() {
        return contact;
    }

    @JsonProperty("contact")
    public void setContact(String contact) {
        this.contact = contact;
    }

    @JsonProperty("vendorAffiliation")
    public String getVendorAffiliation() {
        return vendorAffiliation;
    }

    @JsonProperty("vendorAffiliation")
    public void setVendorAffiliation(String vendorAffiliation) {
        this.vendorAffiliation = vendorAffiliation;
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