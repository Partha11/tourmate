package com.techmave.tourmate.pojo;

public class NearbyPlaces {

    private String placeName;
    private String placeAddress;

    public NearbyPlaces(String placeName, String placeAddress) {

        this.placeName = placeName;
        this.placeAddress = placeAddress;
    }

    public String getPlaceName() {

        return placeName;
    }

    public String getPlaceAddress() {

        return placeAddress;
    }
}
