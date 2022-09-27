package com.techmave.tourmate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.techmave.tourmate.R;
import com.techmave.tourmate.pojo.Event;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {

        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.model_view_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.eventTitle.setText(eventList.get(position).getDescription());
        holder.eventStartDate.setText(eventList.get(position).getEventTag());
        holder.eventLocation.setText(StringUtils.capitalize(eventList.get(position).getDestination()));
        holder.eventBudget.setText(eventList.get(position).getBudget());
    }

    @Override
    public int getItemCount() {

        return eventList == null ? 0 : eventList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_title)
        AppCompatTextView eventTitle;
        @BindView(R.id.event_budget)
        AppCompatTextView eventBudget;
        @BindView(R.id.event_location)
        AppCompatTextView eventLocation;
        @BindView(R.id.event_start_date)
        AppCompatTextView eventStartDate;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.event_root)
        public void onClick(View view) {

            Toast.makeText(context, "Clicked " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
