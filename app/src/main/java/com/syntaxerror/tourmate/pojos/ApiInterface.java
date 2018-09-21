package com.syntaxerror.tourmate.pojos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("place/nearbysearch/json?")
    Call<Nearby> searchPlaces (@Query("location") String location,
                                    @Query("keyword") String keyword,
                                    @Query("opennow") String opennow,
                                    @Query("rankby") String rankby,
                                    @Query("key") String key);

    /*@GET("distancematrix/json")
    Call<DistanceLine.ResultDistanceMatrix> getDistance (@Query("key") String key,
                                                         @Query("origins") String origins,
                                                         @Query("destinations") String destinations);*/
}
