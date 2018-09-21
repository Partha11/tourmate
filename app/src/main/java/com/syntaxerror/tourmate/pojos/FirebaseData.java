package com.syntaxerror.tourmate.pojos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseData {

    private static DatabaseReference dbReference;
    private static DatabaseReference dbEventNode;
    private static DatabaseReference dbExpenseNode;

    private static boolean returnValue;

    private List<Events> eventsList;
    private List<Expenses> expensesList;

    private static SharedPrefData sharedPrefData;

    public FirebaseData(Context context) {

        dbReference = FirebaseDatabase.getInstance().getReference();
        dbEventNode = dbReference.child("Events");
        dbExpenseNode = dbReference.child("Expenses");

        sharedPrefData = new SharedPrefData(context);
    }

    public boolean addEvent(Events event) {

        final String eventId = dbEventNode.push().getKey();

        Events customEvent = new Events(eventId, event.getTravelDescription(), event.getEstimatedBudget(), event.getFromDate(),
                event.getToDate());

        dbEventNode.child(eventId).setValue(customEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                returnValue = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                returnValue = false;

                Log.e("Error", e.getLocalizedMessage());
            }
        });

        return returnValue;
    }

    public boolean addExpense(Expenses expense) {

        final String expenseId = dbExpenseNode.push().getKey();

        dbExpenseNode.child(expense.getEventId())
                .child(expenseId)
                .setValue(expense)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {

                returnValue = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                returnValue = false;

                Log.e("Error", e.getLocalizedMessage());
            }
        });

        return returnValue;
    }

    public List<Expenses> fetchExpenses(String id) {

        expensesList = new ArrayList<>();
        expensesList.clear();

        dbExpenseNode.child(id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    expensesList.add(i.getValue(Expenses.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Failed", databaseError.getMessage());
            }
        });

        return expensesList;
    }
}
