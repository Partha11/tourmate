package com.syntaxerror.tourmate.pojos;

import java.util.List;

public class StaticData {

    private static int numberOfEvents;
    private static int totalExpenseAmount;

    public static void setNumberOfEvents(int numberOfEvents) {

        StaticData.numberOfEvents = numberOfEvents;
    }

    public static void setTotalExpenseAmount(List<Expenses> expensesList) {

        totalExpenseAmount = 0;

        for (Expenses i : expensesList) {

            totalExpenseAmount += Integer.parseInt(i.getExpenseAmount());
        }
    }

    public static int getNumberOfEvents() {

        return numberOfEvents;
    }

    public static int getTotalExpenseAmount() {
        return totalExpenseAmount;
    }
}
