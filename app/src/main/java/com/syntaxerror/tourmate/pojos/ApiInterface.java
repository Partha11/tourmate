package com.syntaxerror.tourmate.pojos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("place/nearbysearch/json?")
    Call<Nearby> getNearbyPlaces(@Query("type") String type,
                                 @Query("location") String location,
                                 @Query("radius") int radius,
                                 @Query("key") String key);

    @GET("place/nearbysearch/json?")
    Call<Nearby> searchPlaces (@Query("location") String location,
                                    @Query("keyword") String keyword,
                                    @Query("opennow") String opennow,
                                    @Query("radius") String radius,
                                    @Query("key") String key);

    /*@GET("distancematrix/json")
    Call<DistanceLine.ResultDistanceMatrix> getDistance (@Query("key") String key,
                                                         @Query("origins") String origins,
                                                         @Query("destinations") String destinations);*/
}
