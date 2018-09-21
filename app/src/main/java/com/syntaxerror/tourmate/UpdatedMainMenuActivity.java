package com.syntaxerror.tourmate;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.syntaxerror.tourmate.adapters.EventAdapter;
import com.syntaxerror.tourmate.database.DatabaseManager;
import com.syntaxerror.tourmate.pojos.Events;
import com.syntaxerror.tourmate.pojos.Expenses;
import com.syntaxerror.tourmate.pojos.FirebaseData;
import com.syntaxerror.tourmate.pojos.NearbyPlaceData;
import com.syntaxerror.tourmate.pojos.SharedPrefData;
import com.syntaxerror.tourmate.pojos.SingleUser;

import java.util.ArrayList;
import java.util.List;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import io.github.kobakei.materialfabspeeddial.FabSpeedDial;

public class UpdatedMainMenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        RadioRealButtonGroup.OnClickedButtonListener, ViewEventsFragment.OnFragmentInteractionListener,
        ViewExpensesFragment.OnFragmentInteractionListener, AddEventFragment.OnFragmentInteractionListener,
        AddExpenseFragment.OnFragmentInteractionListener {

    private BottomNavigationView navigation;

    private RadioRealButtonGroup radioButtonGroup;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ViewEventsFragment viewEventsFragment;
    private ViewExpensesFragment viewExpensesFragment;

    private static FirebaseData firebaseData;
    private static DatabaseManager dbManager;

    private static DatabaseReference dbReference;
    private static DatabaseReference dbEventNode;
    private static DatabaseReference dbExpenseNode;

    private List<Events> eventsList;
    private List<Expenses> expensesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_main_menu);

        initFields();
        fetchEvents();
        loadViewEventsFragment();
    }

    private void initFields() {

        dbReference = FirebaseDatabase.getInstance().getReference();
        dbEventNode = dbReference.child("Events");
        dbExpenseNode = dbReference.child("Expenses");

        navigation = findViewById(R.id.navigation);
        radioButtonGroup = findViewById(R.id.radioButtonGroup);

        fragmentManager = getSupportFragmentManager();

        viewEventsFragment = new ViewEventsFragment();
        viewExpensesFragment = new ViewExpensesFragment();

        firebaseData = new FirebaseData(UpdatedMainMenuActivity.this);
        dbManager = new DatabaseManager(this);

        radioButtonGroup.setPosition(0);

        navigation.setOnNavigationItemSelectedListener(this);
        radioButtonGroup.setOnClickedButtonListener(this);
    }

    public void fetchEvents() {

        eventsList = new ArrayList<>();

        dbEventNode.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventsList.clear();

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    Events singleEvent = i.getValue(Events.class);
                    dbManager.insertEventData(singleEvent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Error Occurred", databaseError.getMessage());
            }
        });
    }

    public List<Expenses> fetchExpenses(String id) {

        expensesList = new ArrayList<>();

        Log.e("Event ID", id);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.navigation_home:

                Toast.makeText(UpdatedMainMenuActivity.this, "Home", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.navigation_dashboard:

                Toast.makeText(UpdatedMainMenuActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.navigation_maps:

                Toast.makeText(UpdatedMainMenuActivity.this, "Maps", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.navigation_memories:

                Toast.makeText(UpdatedMainMenuActivity.this, "Memories", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }

    @Override
    public void onClickedButton(RadioRealButton button, int position) {

        if (position == 0)

            loadViewEventsFragment();

        else if (position == 1)

            loadViewExpenseFragment();
    }

    private void loadViewEventsFragment() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, viewEventsFragment);
        fragmentTransaction.commit();
    }

    private void loadViewExpenseFragment() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, viewExpensesFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public List<Expenses> fetchExpenseData(String id) {

        return fetchExpenses(id);
    }

    @Override
    public void addExpense(Expenses expense) {

        if (firebaseData.addExpense(expense))

            Log.e("Expense", "added");
    }

    @Override
    public void addEventDetails(Events event) {

        firebaseData.addEvent(event);
        fetchEvents();
    }

    @Override
    public List<Events> getAllEvents() {

        List<Events> mEventsList = dbManager.getAllEventsData();

        if (mEventsList != null) {

            fetchEvents();
            Log.e("Event Data", String.valueOf(mEventsList.size()));
        }

        else

            fetchEvents();

        return mEventsList;
    }
}
