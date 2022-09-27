package com.techmave.tourmate.model;

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

/*    @GET("data/2.5/weather?")
    Call<WeatherData> getCurrentWeather(@Query("lat") String lat,
                                        @Query("lon") String lon,
                                        @Query("APPID") String appid);*/

    @GET("data/2.5/forecast?")
    Call<WeatherAdvanced> getWeatherInfos(@Query("q") String q,
                                      @Query("units") String units,
                                      @Query("APPID") String appid);
}
