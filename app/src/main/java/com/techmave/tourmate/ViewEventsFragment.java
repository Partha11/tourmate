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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techmave.tourmate.adapters.EventAdapter;
import com.techmave.tourmate.database.DatabaseManager;
import com.techmave.tourmate.pojo.Event;
import com.techmave.tourmate.pojo.StaticData;
import com.techmave.tourmate.pojo.SwipeDismissListener;
import com.techmave.tourmate.view.activity.DashboardActivity;

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
    private List<Event> eventList;

    private FabSpeedDial fab;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private DatabaseReference dbEventNode;
    private DatabaseReference dbExpenseNode;
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

        dbEventNode = FirebaseDatabase.getInstance().getReference().child("gg").child("Event");
        dbExpenseNode = FirebaseDatabase.getInstance().getReference().child("gg").child("Expenses");

        mListView = view.findViewById(R.id.viewEventsList);
        fab = view.findViewById(R.id.fabEvents);
        fragmentManager = getActivity().getSupportFragmentManager();
        addEvent = new AddEventFragment();

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(mContext, R.layout.view_event_model, eventList);
        eventAdapter.notifyDataSetChanged();
        mListView.setAdapter(eventAdapter);

        dbEventNode.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventList.clear();

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    Event singleEvent = i.getValue(Event.class);
                    eventList.add(singleEvent);
                    dbManager.insertEventData(singleEvent);

                    eventAdapter.notifyDataSetChanged();
                }

                StaticData.setNumberOfEvents(eventList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Error Occurred", databaseError.getMessage());
            }
        });

        StaticData.setTotalExpenseAmount(dbManager.getAllEventsData());
        fab.addOnMenuItemClickListener(this);

        SwipeDismissListener swipeDismissListener = new SwipeDismissListener(mListView, new SwipeDismissListener.DismissCallbacks() {

            @Override
            public boolean canDismiss(int position) {

                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {

                for (int i : reverseSortedPositions) {

                    final String eventId = "ff";
                    final int id = i;

                    dbEventNode.child(eventId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            dbExpenseNode.child(eventId).removeValue();
                            eventList.remove(id);
                            dbManager.deleteSingleEvent(eventId);
                            eventAdapter.notifyDataSetChanged();

                            Toast.makeText(mContext, "Event Removed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(mContext, "An Error Occurred", Toast.LENGTH_SHORT).show();
                            Log.d("ViewEvents", e.getLocalizedMessage());
                        }
                    });
                }
            }
        });

        mListView.setOnTouchListener(swipeDismissListener);

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

        getActivity().setTitle("Event");
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

        fragmentTransaction.replace(R.id.frame_layout, new AddEventFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addExpenseClicked() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, new AddExpenseFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
