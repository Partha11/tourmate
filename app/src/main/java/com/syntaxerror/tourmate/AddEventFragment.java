package com.syntaxerror.tourmate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.syntaxerror.tourmate.pojos.Events;

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

        travelDescription = view.findViewById(R.id.travelDestination);
        travelFromDate = view.findViewById(R.id.fromDate);
        travelToDate = view.findViewById(R.id.toDate);
        travelEstBudget = view.findViewById(R.id.estimatedBudget);

        createEventButton = view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(this);

        return view;
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

                Events event = new Events(mTravelDescription, mTravelEstBudget, mTravelFromDate, mTravelToDate);
                mListener.addEventDetails(event);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void addEventDetails(Events event);
    }
}
