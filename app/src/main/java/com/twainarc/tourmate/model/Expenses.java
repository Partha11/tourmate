package com.twainarc.tourmate.model;

public class Expenses {

    private String eventId;
    private String time;
    private String expenseDetails;
    private String expenseAmount;
    private String eventDetails;

    public Expenses() {

    }

    public Expenses(String eventId, String time, String expenseDetails, String expenseAmount, String eventDetails) {

        this.eventId = eventId;
        this.time = time;
        this.expenseDetails = expenseDetails;
        this.expenseAmount = expenseAmount;
        this.eventDetails = eventDetails;
    }

    public Expenses(String eventId, String time, String expenseDetails, String expenseAmount) {

        this.eventId = eventId;
        this.time = time;
        this.expenseDetails = expenseDetails;
        this.expenseAmount = expenseAmount;
    }

    public String getEventDetails() {

        return eventDetails;
    }

    public String getEventId() {

        return eventId;
    }

    public String getTime() {

        return time;
    }

    public String getExpenseDetails() {

        return expenseDetails;
    }

    public String getExpenseAmount() {

        return expenseAmount;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setExpenseDetails(String expenseDetails) {
        this.expenseDetails = expenseDetails;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }
}
