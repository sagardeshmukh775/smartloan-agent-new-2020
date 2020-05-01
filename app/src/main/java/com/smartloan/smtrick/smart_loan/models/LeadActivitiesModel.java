package com.smartloan.smtrick.smart_loan.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.Map;

public class LeadActivitiesModel implements Serializable, Cloneable {
    private String leadId;
    private String activityType;
    private String activityKey;
    private String activityTitle;
    private String activityDoneByName;
    private String activityNote;
    private Long createdDateTime;

    @Override
    public LeadActivitiesModel clone() throws CloneNotSupportedException {
        return (LeadActivitiesModel) super.clone();
    }

    public LeadActivitiesModel() {
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityDoneByName() {
        return activityDoneByName;
    }

    public void setActivityDoneByName(String activityDoneByName) {
        this.activityDoneByName = activityDoneByName;
    }

    public String getActivityNote() {
        return activityNote;
    }

    public void setActivityNote(String activityNote) {
        this.activityNote = activityNote;
    }

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    @Exclude
    public Long getCreatedDateTimeLong() {
        return createdDateTime;
    }

    public Map<String, String> getCreatedDateTime() {
        return ServerValue.TIMESTAMP;
    }

    public void setCreatedDateTime(Long createdDateTime) {
        this.createdDateTime = (Long) createdDateTime;
    }
}
