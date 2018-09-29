package com.syntaxerror.tourmate.pojos;

import android.net.Uri;

import java.util.List;

public class Events {

    private String eventId;
    private String travelDescription;
    private String estimatedBudget;
    private String fromDate;
    private String toDate;
    private ImageList imageList;

    public Events() {

    }

    public Events(String eventId, String travelDescription, String estimatedBudget, String fromDate, String toDate, ImageList imageList) {

        this.eventId = eventId;
        this.travelDescription = travelDescription;
        this.estimatedBudget = estimatedBudget;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.imageList = imageList;
    }

    public Events(String eventId, String travelDescription, String estimatedBudget, String fromDate, String toDate) {

        this.eventId = eventId;
        this.travelDescription = travelDescription;
        this.estimatedBudget = estimatedBudget;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Events(String travelDescription, String estimatedBudget, String fromDate, String toDate) {

        this.travelDescription = travelDescription;
        this.estimatedBudget = estimatedBudget;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public ImageList getImageList() {

        return imageList;
    }

    public String getEventId() {

        return eventId;
    }

    public String getTravelDescription() {

        return travelDescription;
    }

    public String getEstimatedBudget() {

        return estimatedBudget;
    }

    public String getFromDate() {

        return fromDate;
    }

    public String getToDate() {

        return toDate;
    }
}
