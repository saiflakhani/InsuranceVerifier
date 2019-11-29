package com.quicsolv.insurance.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerifierAssigned implements Serializable {

@SerializedName("verifierName")
@Expose
private String verifierName;
@SerializedName("varifierNum")
@Expose
private String varifierNum;

public String getVerifierName() {
return verifierName;
}

public void setVerifierName(String verifierName) {
this.verifierName = verifierName;
}

public String getVarifierNum() {
return varifierNum;
}

public void setVarifierNum(String varifierNum) {
this.varifierNum = varifierNum;
}

}