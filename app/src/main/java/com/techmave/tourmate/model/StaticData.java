package com.techmave.tourmate.model;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StaticData {

    private static int numberOfEvents;
    private static int totalExpenseAmount;

    private static final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference()
            .child("gg").child("Expenses");

    public static void setNumberOfEvents(int numberOfEvents) {

        StaticData.numberOfEvents = numberOfEvents;
    }

    public static void setTotalExpenseAmount(List<Event> eventList) {

        totalExpenseAmount = 0;

        Log.d("StaticData", "EventsList Size- " + String.valueOf(eventList.size()));

        for (Event event : eventList) {

            dbReference.child("gg").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot i : dataSnapshot.getChildren()) {

                        totalExpenseAmount += Integer.parseInt(i.getValue(Expenses.class).getExpenseAmount());

                        Log.d("StaticData", String.valueOf(i.getValue(Expenses.class).getExpenseAmount()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.d("StaticData", databaseError.getMessage());
                }
            });
        }

        Log.d("StaticData", String.valueOf(totalExpenseAmount));
    }

    public static int getNumberOfEvents() {

        return numberOfEvents;
    }

    public static int getTotalExpenseAmount() {
        return totalExpenseAmount;
    }
}
