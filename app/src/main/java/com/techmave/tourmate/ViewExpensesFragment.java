package com.techmave.tourmate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techmave.tourmate.adapters.ExpenseAdapter;
import com.techmave.tourmate.database.DatabaseManager;
import com.techmave.tourmate.pojo.Event;
import com.techmave.tourmate.pojo.Expenses;
import com.techmave.tourmate.view.activity.DashboardActivity;

import java.util.ArrayList;
import java.util.List;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;

public class ViewExpensesFragment extends Fragment implements FabSpeedDial.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;

    private List<Expenses> expensesList;
    private List<Event> eventList;

    private ListView mListView;
    private ExpenseAdapter expenseAdapter;
    private Spinner mSpinner;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private AddEventFragment addEvent;
    private AddExpenseFragment addExpense;

    private FabSpeedDial fab;

    private DatabaseReference dbExpenseNode;
    private DatabaseManager dbManager;

    public ViewExpensesFragment() {
        // Required empty public constructor
    }

    public static ViewExpensesFragment newInstance(String param1, String param2) {
        ViewExpensesFragment fragment = new ViewExpensesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_expenses, container, false);

        getActivity().setTitle("Expenses");

        fab = view.findViewById(R.id.fabExpense);
        mSpinner = view.findViewById(R.id.expenseSpinner);
        mListView = view.findViewById(R.id.viewExpensesList);

        addEvent = new AddEventFragment();
        addExpense = new AddExpenseFragment();
        fragmentManager = getActivity().getSupportFragmentManager();

        fab.addOnMenuItemClickListener(this);

        expensesList = new ArrayList<>();
        eventList = dbManager.getAllEventsData();

        if (eventList == null)

            eventList = new ArrayList<>();

        else

            Log.d("Eventslist", eventList.toString());

        initSpinner();

        expenseAdapter = new ExpenseAdapter(mContext, R.layout.view_expense_model, expensesList);
        expenseAdapter.notifyDataSetChanged();
        mListView.setAdapter(expenseAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (eventList != null && eventList.size() > 0) {

                    String eventId = "gg";

                    Log.d("EventId", eventId);

                    dbExpenseNode = FirebaseDatabase.getInstance().getReference().child("gg")
                            .child("Expenses").child(eventId);
                    dbExpenseNode.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            expensesList.clear();

                            for (DataSnapshot i : dataSnapshot.getChildren()) {

                                Expenses expenses = i.getValue(Expenses.class);
                                expensesList.add(expenses);

                                Log.d("Expenses", expenses.getEventId());
                            }

                            expenseAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Log.d("Expenses", databaseError.getMessage());
                        }
                    });
                }

                else

                    Log.e("Place Data", "null");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mListView.setOnItemClickListener(this);

        return view;
    }

    private void initSpinner() {

        if (eventList != null) {

            Log.d("Eventslist Size", String.valueOf(eventList.size()));

            List<String> nameList = new ArrayList<>();

            for (Event i : eventList) {

                String data = i.getDescription() + "(" + i.getFromDate() + ")";
                nameList.add(data);
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, nameList);
            mSpinner.setAdapter(spinnerAdapter);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

        if (mListener != null) {

            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {

            mListener = (OnFragmentInteractionListener) context;
        }

        else {

            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        mContext = context;
        dbManager = new DatabaseManager(mContext);
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMenuItemClick(FloatingActionButton miniFab, @Nullable TextView label, int itemId) {

        if (label.getText().toString().equals("Add Event"))

            addTravelEventClicked();

        else

            addExpenseClicked();
    }

    private void addTravelEventClicked() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, addEvent);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addExpenseClicked() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, addExpense);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
