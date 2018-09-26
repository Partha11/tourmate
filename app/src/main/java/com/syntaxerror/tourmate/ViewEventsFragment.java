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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.syntaxerror.tourmate.pojos.NearbyPlaceData;
import com.syntaxerror.tourmate.pojos.StaticData;

import java.util.ArrayList;
import java.util.List;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;

public class ViewEventsFragment extends Fragment implements FabSpeedDial.OnMenuItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;
    private ListView mListView;
    private EventAdapter eventAdapter;
    private List<Events> eventsList;

    private FabSpeedDial fab;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private DatabaseReference dbEventNode;
    private DatabaseManager dbManager;

    private AddEventFragment addEvent;

    public ViewEventsFragment() {
        // Required empty public constructor
    }

    public static ViewEventsFragment newInstance(String param1, String param2) {
        ViewEventsFragment fragment = new ViewEventsFragment();
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

        View view = inflater.inflate(R.layout.fragment_view_events, container, false);

        dbEventNode = FirebaseDatabase.getInstance().getReference().child(UpdatedMainMenuActivity.userId).child("Events");

        mListView = view.findViewById(R.id.viewEventsList);
        fab = view.findViewById(R.id.fabEvents);
        fragmentManager = getActivity().getSupportFragmentManager();
        addEvent = new AddEventFragment();

        eventsList = new ArrayList<>();
        eventAdapter = new EventAdapter(mContext, R.layout.view_event_model, eventsList);
        eventAdapter.notifyDataSetChanged();
        mListView.setAdapter(eventAdapter);

        dbEventNode.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    Events singleEvent = i.getValue(Events.class);
                    eventsList.add(singleEvent);
                    dbManager.insertEventData(singleEvent);

                    eventAdapter.notifyDataSetChanged();
                }

                StaticData.setNumberOfEvents(eventsList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Error Occurred", databaseError.getMessage());
            }
        });

        fab.addOnMenuItemClickListener(this);

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

        getActivity().setTitle("Events");
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

        if (label.getText().toString().trim().equals("Add Event"))

            addTravelEventClicked();

        else

            addExpenseClicked();
    }

    private void addTravelEventClicked() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, new AddEventFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addExpenseClicked() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, new AddExpenseFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
