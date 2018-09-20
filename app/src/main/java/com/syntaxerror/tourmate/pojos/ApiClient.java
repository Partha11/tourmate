package com.syntaxerror.tourmate.pojos;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static final String GOOGLE_PLACE_API_KEY = "AIzaSyAgTAllKNklF8aC1oiYr9TNEszv9LpSStM";

    public static String base_url = "https://maps.googleapis.com/maps/api/";

    public static Retrofit getClient() {

/*        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).addInterceptor(interceptor).build();*/

        retrofit = null;
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
