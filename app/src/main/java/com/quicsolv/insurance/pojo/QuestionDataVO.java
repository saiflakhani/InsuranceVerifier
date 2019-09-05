package com.quicsolv.insurance.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuestionDataVO implements Serializable {

    @SerializedName("questionID")
    @Expose
    private String questionID;
    @SerializedName("questionText")
    @Expose
    private String questionText;
    @SerializedName("questionRequired")
    @Expose
    private String questionRequired;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @SerializedName("answer")
    @Expose
    private String answer;

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionRequired() {
        return questionRequired;
    }

    public void setQuestionRequired(String questionRequired) {
        this.questionRequired = questionRequired;
    }

}
