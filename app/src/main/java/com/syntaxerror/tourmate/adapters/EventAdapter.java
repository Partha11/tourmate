package com.syntaxerror.tourmate.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.syntaxerror.tourmate.R;
import com.syntaxerror.tourmate.pojos.Events;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Events> {

    private Context mContext;
    private List<Events> eventsList;
    private int mResource;

    private TextView eventTitle;
    private TextView fromDate;
    private TextView toDate;
    private TextView estBudget;

    public EventAdapter(@NonNull Context context, int resource, @NonNull List<Events> objects) {

        super(context, resource, objects);

        mContext = context;
        eventsList = objects;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = super.getView(position, convertView, parent);
        LayoutInflater inflater = LayoutInflater.from(mContext);

        inflater.inflate(mResource, parent, false);

        eventTitle = convertView.findViewById(R.id.eventTitle);
        fromDate = convertView.findViewById(R.id.eventFromDate);
        toDate = convertView.findViewById(R.id.eventToDate);
        estBudget = convertView.findViewById(R.id.eventEstimatedBudget);

        eventTitle.setText(eventsList.get(position).getTravelDescription());
        fromDate.setText(eventsList.get(position).getFromDate());
        toDate.setText(eventsList.get(position).getToDate());
        estBudget.setText(eventsList.get(position).getEstimatedBudget());

        return convertView;
    }
}
