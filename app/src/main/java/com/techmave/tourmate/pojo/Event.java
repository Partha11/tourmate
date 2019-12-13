package com.techmave.tourmate.pojo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.techmave.tourmate.utils.Constants;

@Entity(tableName = Constants.TABLE_EVENTS)
public class Event {

    @ColumnInfo(name = Constants.EVENT_ID)
    @PrimaryKey
    @SerializedName("id")
    @NonNull
    private String id = "";
    @ColumnInfo(name = Constants.EVENT_DESCRIPTION)
    @SerializedName("description")
    private String description;
    @ColumnInfo(name = Constants.EVENT_BUDGET)
    @SerializedName("budget")
    private String budget;
    @ColumnInfo(name = Constants.EVENT_START_DATE)
    @SerializedName("fromDate")
    private String fromDate;
    @ColumnInfo(name = Constants.EVENT_END_DATE)
    @SerializedName("toDate")
    private String toDate;
    @Ignore
    @SerializedName("destination")
    private String destination;
    @Ignore
    @SerializedName("eventTag")
    private String eventTag;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEventTag() {
        return eventTag;
    }

    public void setEventTag(String eventTag) {
        this.eventTag = eventTag;
    }
}
