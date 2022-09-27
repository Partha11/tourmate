package com.techmave.tourmate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.techmave.tourmate.adapters.NearbyPlacesAdapter;
import com.techmave.tourmate.model.NearbyPlaceData;
import com.techmave.tourmate.model.NearbyPlaces;

import java.util.List;

public class DisplayNearbyPlacesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;

    private ListView mListView;
    private NearbyPlacesAdapter nearbyPlacesAdapter;

    private List<NearbyPlaces> nearbyPlacesList;

    public DisplayNearbyPlacesFragment() {
        // Required empty public constructor
    }

    public static DisplayNearbyPlacesFragment newInstance(String param1, String param2) {
        DisplayNearbyPlacesFragment fragment = new DisplayNearbyPlacesFragment();
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

        View view = inflater.inflate(R.layout.fragment_display_nearby_places, container, false);

        mListView = view.findViewById(R.id.nearbyPlacesCategorized);

        nearbyPlacesList = NearbyPlaceData.nearbyPlaces;

        if (nearbyPlacesList != null) {

            Log.e("Places", nearbyPlacesList.toString());

            nearbyPlacesAdapter = new NearbyPlacesAdapter(mContext, R.layout.view_nearbyplaces_model, nearbyPlacesList);
            mListView.setAdapter(nearbyPlacesAdapter);
        }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
