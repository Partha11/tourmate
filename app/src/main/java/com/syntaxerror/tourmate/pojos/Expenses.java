package com.syntaxerror.tourmate.pojos;

public class Expenses {

    private String eventId;
    private String time;
    private String expenseDetails;
    private String expenseAmount;

    public Expenses() {

    }

    public Expenses(String eventId, String time, String expenseDetails, String expenseAmount) {

        this.eventId = eventId;
        this.time = time;
        this.expenseDetails = expenseDetails;
        this.expenseAmount = expenseAmount;
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
}
