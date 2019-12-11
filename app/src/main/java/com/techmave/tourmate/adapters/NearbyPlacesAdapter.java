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
import com.techmave.tourmate.pojo.NearbyPlaces;

import java.util.List;

public class NearbyPlacesAdapter extends ArrayAdapter<NearbyPlaces> {

    private Context mContext;
    private List<NearbyPlaces> nearbyPlacesList;
    private int mResource;

    private TextView placeName;
    private TextView placeAddress;

    public NearbyPlacesAdapter(@NonNull Context context, int resource, @NonNull List<NearbyPlaces> objects) {

        super(context, resource, objects);

        mContext = context;
        nearbyPlacesList = objects;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        placeName = convertView.findViewById(R.id.placeName);
        placeAddress = convertView.findViewById(R.id.placeAddress);

        placeName.setText(nearbyPlacesList.get(position).getPlaceName());
        placeAddress.setText(nearbyPlacesList.get(position).getPlaceAddress());

        return convertView;
    }
}
