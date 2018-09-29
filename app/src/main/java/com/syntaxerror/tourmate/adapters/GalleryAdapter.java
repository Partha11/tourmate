package com.syntaxerror.tourmate.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.syntaxerror.tourmate.R;
import com.syntaxerror.tourmate.pojos.SingleImage;

import java.util.List;

public class GalleryAdapter extends ArrayAdapter<Uri> {

    private Context mContext;
    private List<Uri> imageLists;
    private int mResource;

    private ImageView firstImg;
    private ImageView secondImg;
    private ImageView thirdImg;
    private ImageView fourthImg;
    private ImageView fifthImg;
    private ImageView sixthImg;

    public GalleryAdapter(@NonNull Context context, int resource, @NonNull List<Uri> objects) {

        super(context, resource, objects);

        mContext = context;
        imageLists = objects;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        firstImg = convertView.findViewById(R.id.firstImg);
        secondImg = convertView.findViewById(R.id.secondImg);
        thirdImg = convertView.findViewById(R.id.thirdImg);
        fourthImg = convertView.findViewById(R.id.fourthImg);
        fifthImg = convertView.findViewById(R.id.fifthImg);
        sixthImg = convertView.findViewById(R.id.sixthImg);

        if (imageLists.get(position) != null) {

            if (position == 0) {

                Picasso.get()
                        .load(imageLists.get(position))
                        .into(firstImg);
            }

            else if (position == 1) {

                Picasso.get()
                        .load(imageLists.get(position))
                        .into(secondImg);
            }

            else if (position == 3) {

                Picasso.get()
                        .load(imageLists.get(position))
                        .into(thirdImg);
            }

            else if (position == 4) {

                Picasso.get()
                        .load(imageLists.get(position))
                        .into(fourthImg);
            }

            else if (position == 5) {

                Picasso.get()
                        .load(imageLists.get(position))
                        .into(fifthImg);
            }

            else if (position == 6) {

                Picasso.get()
                        .load(imageLists.get(position))
                        .into(sixthImg);
            }
        }


        return convertView;
    }
}
