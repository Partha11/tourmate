package com.techmave.tourmate.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherClient {

    private static Retrofit retrofit;

    public static final String OPENWEATHER_API_KEY = "cd1af58cd39b242a7df71fbb0f5ebf7b";

    public static String weather_base_url = "https://api.openweathermap.org/";

    public static Retrofit getClient() {

        retrofit = null;
        retrofit = new Retrofit.Builder()
                .baseUrl(weather_base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
