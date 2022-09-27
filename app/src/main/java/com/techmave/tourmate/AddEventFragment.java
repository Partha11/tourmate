package com.techmave.tourmate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.techmave.tourmate.model.Event;

import java.util.Calendar;

public class AddEventFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;

    private EditText travelDescription;
    private EditText travelFromDate;
    private EditText travelToDate;
    private EditText travelEstBudget;

    private Button createEventButton;

    private String selectedDate;

    public AddEventFragment() {
        // Required empty public constructor
    }

    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
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

        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        getActivity().setTitle("Add Event");

        travelDescription = view.findViewById(R.id.travelDestination);
        travelFromDate = view.findViewById(R.id.fromDate);
        travelToDate = view.findViewById(R.id.toDate);
//        travelEstBudget = view.findViewById(R.id.estimatedBudget);

        createEventButton = view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(this);
        travelToDate.setOnClickListener(this);
        travelFromDate.setOnClickListener(this);

        return view;
    }

    private String datePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                selectedDate = day + "-" + (month + 1) + "-" + year;
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();

        return selectedDate;
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

        if (v == createEventButton) {

            String mTravelDescription = travelDescription.getText().toString().trim();
            String mTravelEstBudget = travelEstBudget.getText().toString().trim();
            String mTravelFromDate = travelFromDate.getText().toString().trim();
            String mTravelToDate = travelToDate.getText().toString().trim();

            if (TextUtils.isEmpty(mTravelDescription))

                travelDescription.setError("Please Enter Travel Description");

            else if (TextUtils.isEmpty(mTravelFromDate))

                travelFromDate.setError("Please Select Date");

            else if (TextUtils.isEmpty(mTravelToDate))

                travelToDate.setError("Please Select Date");

            else if (TextUtils.isEmpty(mTravelEstBudget))

                travelEstBudget.setError("Please Enter Budget");

            else {

                Event event = new Event();
                mListener.addEventDetails(event);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.frame_layout, new ViewEventsFragment());
                fragmentTransaction.commit();
            }
        }

        else if (v == travelToDate) {

            travelToDate.setText(datePicker());
        }

        else if (v == travelFromDate) {

            travelFromDate.setText(datePicker());
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void addEventDetails(Event event);
    }
}
