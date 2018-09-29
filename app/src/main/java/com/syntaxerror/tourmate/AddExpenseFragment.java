package com.syntaxerror.tourmate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.syntaxerror.tourmate.pojos.Events;
import com.syntaxerror.tourmate.pojos.Expenses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddExpenseFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;

    private Spinner mSpinner;
    private EditText eventExpenseAmount;
    private TextView eventExpenseDetails;

    private List<Events> eventsList;

    private String eventId;
    private String eventDescription;

    private Button mSaveButton;

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    public static AddExpenseFragment newInstance(String param1, String param2) {

        AddExpenseFragment fragment = new AddExpenseFragment();
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

        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        mSpinner = view.findViewById(R.id.eventSpinner);
        eventExpenseAmount = view.findViewById(R.id.eventExpenseAmount);
        mSaveButton = view.findViewById(R.id.saveExpense);
        eventExpenseDetails = view.findViewById(R.id.expenseDetails);

        eventsList = new ArrayList<>();
        eventsList = mListener.getAllEvents();

        initSpinner();

        mSaveButton.setOnClickListener(this);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                eventId = eventsList.get(position).getEventId();
                eventDescription = eventsList.get(position).getTravelDescription();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void initSpinner() {

        if (eventsList == null) {

            Log.e("EventsList", "Null");
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
            spinnerAdapter.notifyDataSetChanged();
        }
    }

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
    public void onClick(View v) {

        if (v == mSaveButton) {

            if (TextUtils.isEmpty(eventExpenseDetails.getText().toString()))

                eventExpenseDetails.setError("Please Enter Expense Details");

            else if (TextUtils.isEmpty(eventExpenseAmount.getText().toString()))

                eventExpenseAmount.setError("Please Enter Expense Amount");

            else {

                String expenseDetails = eventExpenseDetails.getText().toString().trim();
                String expenseAmount = eventExpenseAmount.getText().toString();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String eventTime = timeFormat.format(calendar.getTime());

                mListener.addExpense(new Expenses(eventId, eventTime, expenseDetails, expenseAmount, eventDescription));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.updatedFragmentLayout, new ViewExpensesFragment());
                fragmentTransaction.commit();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void addExpense(Expenses expense);
        List<Events> getAllEvents();
    }
}
