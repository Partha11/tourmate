package com.syntaxerror.tourmate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.syntaxerror.tourmate.adapters.ExpenseAdapter;
import com.syntaxerror.tourmate.pojos.Events;
import com.syntaxerror.tourmate.pojos.Expenses;
import com.syntaxerror.tourmate.pojos.NearbyPlaceData;

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
    private List<Events> eventsList;

    private ListView mListView;
    private ExpenseAdapter expenseAdapter;
    private Spinner mSpinner;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private AddEventFragment addEvent;
    private AddExpenseFragment addExpense;

    private FabSpeedDial fab;

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

        eventsList = new ArrayList<>();
        eventsList = mListener.getAllEvents();

        initSpinner();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (eventsList != null) {

                    String eventId = eventsList.get(position).getEventId();
                    expensesList = mListener.fetchExpenseData(eventId);

                    if (expensesList != null) {

                        expenseAdapter = new ExpenseAdapter(mContext, R.layout.view_expense_model, expensesList);
                        mListView.setAdapter(expenseAdapter);
                    }
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

        if (eventsList == null) {

            Toast.makeText(mContext, "Something went wrong.Please try again", Toast.LENGTH_SHORT).show();
            eventsList = mListener.getAllEvents();
        }

        else {

            List<String> nameList = new ArrayList<>();

            for (Events i : eventsList) {

                String data = i.getTravelDescription() + "(" + i.getFromDate() + ")";

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

        fragmentTransaction.replace(R.id.updatedFragmentLayout, addEvent);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addExpenseClicked() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, addExpense);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        List<Expenses> fetchExpenseData(String id);
        List<Events> getAllEvents();
    }
}