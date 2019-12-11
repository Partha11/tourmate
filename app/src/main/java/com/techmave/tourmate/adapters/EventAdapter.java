package com.techmave.tourmate.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.techmave.tourmate.R;
import com.techmave.tourmate.pojo.Event;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private Context mContext;
    private List<Event> eventList;
    private int mResource;

    private TextView eventTitle;
    private TextView fromDate;
    private TextView toDate;
    private TextView estBudget;

    public EventAdapter(@NonNull Context context, int resource, @NonNull List<Event> objects) {

        super(context, resource, objects);

        mContext = context;
        eventList = objects;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        eventTitle = convertView.findViewById(R.id.eventTitle);
        fromDate = convertView.findViewById(R.id.eventFromDate);
        toDate = convertView.findViewById(R.id.eventToDate);
        estBudget = convertView.findViewById(R.id.eventEstimatedBudget);

        eventTitle.setText(eventList.get(position).getDescription());
        fromDate.setText(eventList.get(position).getFromDate());
        toDate.setText(eventList.get(position).getToDate());
        estBudget.setText(eventList.get(position).getBudget());

        return convertView;
    }
}
