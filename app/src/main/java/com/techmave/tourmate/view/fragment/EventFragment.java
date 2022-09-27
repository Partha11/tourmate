package com.techmave.tourmate.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.internal.NavigationMenu;
import com.google.firebase.database.DataSnapshot;
import com.techmave.tourmate.R;
import com.techmave.tourmate.adapters.EventAdapter;
import com.techmave.tourmate.dialog.EventDialog;
import com.techmave.tourmate.pojo.Event;
import com.techmave.tourmate.utils.SharedPrefs;
import com.techmave.tourmate.utils.Utility;
import com.techmave.tourmate.viewmodel.DashboardViewModel;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;

public class EventFragment extends Fragment implements FabSpeedDial.MenuListener {

    @BindView(R.id.event_recycler)
    RecyclerView eventRecycler;
    @BindView(R.id.event_fab)
    FabSpeedDial fab;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Context context;
    private EventAdapter adapter;

    private List<Event> eventList;

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        initialize();

        return view;
    }

    private void initialize() {

        SharedPrefs prefs = new SharedPrefs(context);
        DashboardViewModel viewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        eventList = new ArrayList<>();
        adapter = new EventAdapter(context, eventList);

        progressBar.setVisibility(View.VISIBLE);
        fab.setMenuListener(this);

        eventRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        eventRecycler.setItemAnimator(new DefaultItemAnimator());
        eventRecycler.setAdapter(adapter);

        viewModel.setLiveData(prefs.getUid());
        viewModel.getAllEvents().observe(Objects.requireNonNull(getActivity()), dataSnapshot -> {

            if (dataSnapshot.hasChildren()) {

                Date date = new Date();
                DateTime now = new DateTime(date);
                eventList.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    Event event = d.getValue(Event.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                    if (event != null) {

                        try {

                            DateTime end = new DateTime(sdf.parse(event.getToDate()));

                            int diff = Days.daysBetween(end.toLocalDate(), now.toLocalDate()).getDays();

                            if (Utility.compareDates(Objects.requireNonNull(sdf.parse(sdf.format(date))),
                                    sdf.parse(event.getToDate()))) {

                                String text = "Ended: " + diff + "d Ago";
                                event.setEventTag(text);

                            } else {

                                String text = "Ends In: " + String.valueOf(diff).substring(1) + "d";
                                event.setEventTag(text);
                            }

                        } catch (Exception ex) {

                            event.setEventTag("Undetermined");
                            ex.printStackTrace();
                        }

                        eventList.add(event);
                    }
                }

                Log.d("Size", String.valueOf(eventList.size()));
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        this.context = context;
        Objects.requireNonNull(getActivity()).setTitle("Events");
    }

    @Override
    public boolean onPrepareMenu(NavigationMenu navigationMenu) {

        return true;
    }

    @Override
    public boolean onMenuItemSelected(MenuItem menuItem) {

        Log.d("Id", String.valueOf(menuItem.getItemId()));

        if (menuItem.getItemId() == R.id.add_event) {

            DialogFragment fragment = new EventDialog();
            fragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "addEvent");
        }

        return true;
    }

    @Override
    public void onMenuClosed() {
        //Nothing
    }
}
