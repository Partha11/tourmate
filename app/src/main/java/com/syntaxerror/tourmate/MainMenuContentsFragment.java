package com.syntaxerror.tourmate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainMenuContentsFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;

    private FrameLayout fragmentLayout;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private Button addEventButton;
    private Button viewEventButton;

    private AddEventFragment addEvent;
    private ViewEventsFragment viewEvents;

    public MainMenuContentsFragment() {
        // Required empty public constructor
    }

    public static MainMenuContentsFragment newInstance(String param1, String param2) {
        MainMenuContentsFragment fragment = new MainMenuContentsFragment();
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

        View view = inflater.inflate(R.layout.fragment_main_menu_contents, container, false);

        initFields();

        fragmentLayout = view.findViewById(R.id.mainMenuFragment);
        addEventButton = view.findViewById(R.id.addTravelEvent);
        viewEventButton = view.findViewById(R.id.viewTravelEvents);

        addEventButton.setOnClickListener(this);
        viewEventButton.setOnClickListener(this);

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

    private void initFields() {

        addEvent = new AddEventFragment();
        viewEvents = new ViewEventsFragment();

        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.addTravelEvent:

                addTravelEventClicked();
                break;

            case R.id.viewTravelEvents:

                viewTravelEventsClicked();
                break;
        }
    }

    private void addTravelEventClicked() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.mainMenuFragment, addEvent);
        fragmentTransaction.commit();
    }

    private void viewTravelEventsClicked() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.mainMenuFragment, viewEvents);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
