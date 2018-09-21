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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.syntaxerror.tourmate.pojos.NearbyPlaceData;
import com.syntaxerror.tourmate.pojos.NearbyPlaces;

import java.util.ArrayList;
import java.util.List;

public class NearbyPlacesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;

    private ListView mListView;

    private List<String> placesList;
    private List<NearbyPlaces> nearbyPlacesList;

    private ArrayAdapter<String> placesAdapter;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private DisplayNearbyPlacesFragment displayNearbyPlacesFragment;

    public NearbyPlacesFragment() {
        // Required empty public constructor
    }

    public static NearbyPlacesFragment newInstance(String param1, String param2) {

        NearbyPlacesFragment fragment = new NearbyPlacesFragment();
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

        View view = inflater.inflate(R.layout.fragment_nearby_places, container, false);

        initFields();

        mListView = view.findViewById(R.id.nearbyPlacesListView);

        fragmentManager = getActivity().getSupportFragmentManager();

        mListView.setAdapter(placesAdapter);
        mListView.setOnItemClickListener(this);

        return view;
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

    private void initFields() {

        placesList = new ArrayList<>();
        displayNearbyPlacesFragment = new DisplayNearbyPlacesFragment();

        placesList.add("Cafe");
        placesList.add("Restaurant");
        placesList.add("ATM");
        placesList.add("Bank");
        placesList.add("Mosque");

        placesAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, placesList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {

            case 0:

                NearbyPlaceData.nearbyPlaces = mListener.findNearByPlaces("cafe");
                switchFragment();
                break;

            case 1:

                NearbyPlaceData.nearbyPlaces = mListener.findNearByPlaces("restaurant");
                switchFragment();
                break;

            case 2:

                NearbyPlaceData.nearbyPlaces = mListener.findNearByPlaces("atm");
                switchFragment();
                break;

            case 3:

                NearbyPlaceData.nearbyPlaces = mListener.findNearByPlaces("bank");
                switchFragment();
                break;

            case 4:

                NearbyPlaceData.nearbyPlaces = mListener.findNearByPlaces("mosque");
                switchFragment();
                break;
        }
    }

    private void switchFragment() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, displayNearbyPlacesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        List<NearbyPlaces> findNearByPlaces(String placeName);
    }
}
