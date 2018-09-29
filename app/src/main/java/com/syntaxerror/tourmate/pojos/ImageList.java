package com.syntaxerror.tourmate.pojos;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class ImageList {

    private List<Uri> imageList;
    private String dateText;
    int position;

    public ImageList() {

    }

    public ImageList(List<Uri> imageList, String dateText, int position) {

        this.imageList = imageList;
        this.dateText = dateText;
    }

    public String getDateText() {

        return dateText;
    }

    public List<Uri> getImageList() {

        return imageList;
    }

    public int getPosition() {

        return position;
    }
}
