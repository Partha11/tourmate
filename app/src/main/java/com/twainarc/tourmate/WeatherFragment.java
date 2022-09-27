package com.twainarc.tourmate;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.twainarc.tourmate.model.ApiInterface;
import com.twainarc.tourmate.model.WeatherAdvanced;
import com.twainarc.tourmate.model.WeatherClient;
import com.twainarc.tourmate.model.WeatherDataList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFragment extends Fragment {

    private Typeface weatherFont;

    private TextView weatherDetails;
    private TextView placeName;
    private TextView placeTemp;
    private TextView tempFirst;
    private TextView tempSecond;
    private TextView tempThird;
    private TextView tempFourth;
    private TextView tempFifth;
    private TextView dayOne;
    private TextView dayTwo;
    private TextView dayThree;
    private TextView dayFour;
    private TextView dayFive;

    private ImageView imageFirst;
    private ImageView imageSecond;
    private ImageView imageThird;
    private ImageView imageFourth;
    private ImageView imageFifth;

    private long currentTime;

    private List<String> tempList;
    private List<Integer> iconList;
    private List<String> weekDays;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Weather");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_weather, container, false);

        weatherDetails = view.findViewById(R.id.weatherDetails);
        placeName = view.findViewById(R.id.weatherPlaceName);
        placeTemp = view.findViewById(R.id.weatherPlaceTemp);
        tempFirst = view.findViewById(R.id.weatherDayFirst);
        tempSecond = view.findViewById(R.id.weatherDaySecond);
        tempThird = view.findViewById(R.id.weatherDayThird);
        tempFourth = view.findViewById(R.id.weatherDayFourth);
        tempFifth = view.findViewById(R.id.weatherDayFifth);

        dayOne = view.findViewById(R.id.dayOne);
        dayTwo = view.findViewById(R.id.dayTwo);
        dayThree = view.findViewById(R.id.dayThree);
        dayFour = view.findViewById(R.id.dayFour);
        dayFive = view.findViewById(R.id.dayFive);

        imageFirst = view.findViewById(R.id.imageFirst);
        imageSecond = view.findViewById(R.id.imageSecond);
        imageThird = view.findViewById(R.id.imageThird);
        imageFourth = view.findViewById(R.id.imageFourth);
        imageFifth = view.findViewById(R.id.imageFifth);

        tempList = new ArrayList<>();
        iconList = new ArrayList<>();
        weekDays = new ArrayList<>();

        getCurrentTime();

        Log.d("Weather", String.valueOf(currentTime));

        ApiInterface weatherApi = WeatherClient.getClient().create(ApiInterface.class);
        Call<WeatherAdvanced> call = weatherApi.getWeatherInfos("Dhaka", "metric", WeatherClient.OPENWEATHER_API_KEY);
        call.enqueue(new Callback<WeatherAdvanced>() {

            @Override
            public void onResponse(Call<WeatherAdvanced> call, Response<WeatherAdvanced> response) {

                WeatherAdvanced weather = response.body();

                if (weather != null) {

                    List<WeatherDataList> weatherList = weather.getList();

                    int totalRes;
                    int startPoint;

                    Log.d("Weather", String.valueOf(weatherList.size()));

                    if (weatherList.size() >= 40) {

                        totalRes = 40;
                        startPoint = 7;
                    }

                    else {

                        totalRes = 33;
                        startPoint = 0;
                    }

                    for (int i = startPoint; i < totalRes; i += 8) {

                        tempList.add(String.valueOf(weatherList.get(i).getMain().getTempMax()));

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        Date dateFormat = new java.util.Date(weather.getList().get(i).getDt() * 1000);
                        String weekDay = sdf.format(dateFormat);
                        String weekDayInserted = "";

                        if (weekDay.equals("Sunday"))

                            weekDayInserted = "SUN";

                        else if (weekDay.equals("Monday"))

                            weekDayInserted = "MON";

                        else if (weekDay.equals("Tuesday"))

                            weekDayInserted = "TUE";

                        else if (weekDay.equals("Wednesday"))

                            weekDayInserted = "WED";

                        else if (weekDay.equals("Thursday"))

                            weekDayInserted = "THU";

                        else if (weekDay.equals("Friday"))

                            weekDayInserted = "FRI";

                        else if (weekDay.equals("Saturday"))

                            weekDayInserted = "SAT";

                        weekDays.add(weekDayInserted);

                        String iconId = weatherList.get(i).getWeather().get(0).getIcon();
                        int iconRes = R.drawable.ic_sunny;

                        if (TextUtils.equals("01d", iconId))

                            iconRes = R.drawable.ic_sunny;

                        else if (TextUtils.equals("10d", iconId))

                            iconRes = R.drawable.ic_rain_cloud;

                        else if (TextUtils.equals("02d", iconId))

                            iconRes = R.drawable.ic_rain_cloud;

                        iconList.add(iconRes);
                    }

                    weatherDetails.setText(weather.getList().get(0).getWeather().get(0).getDescription());

                    placeName.setText(weather.getCity().getName());
                    placeTemp.setText(String.valueOf(weatherList.get(0).getMain().getTempMax()));
                    tempFirst.setText(tempList.get(0));
                    tempSecond.setText(tempList.get(1));
                    tempThird.setText(tempList.get(2));
                    tempFourth.setText(tempList.get(3));
                    tempFifth.setText(tempList.get(4));

                    dayOne.setText(weekDays.get(0));
                    dayTwo.setText(weekDays.get(1));
                    dayThree.setText(weekDays.get(2));
                    dayFour.setText(weekDays.get(3));
                    dayFive.setText(weekDays.get(4));

                    imageFirst.setImageResource(iconList.get(0));
                    imageSecond.setImageResource(iconList.get(1));
                    imageThird.setImageResource(iconList.get(2));
                    imageFourth.setImageResource(iconList.get(3));
                    imageFifth.setImageResource(iconList.get(4));

/*                    Picasso.get().load(iconList.get(0)).into(imageFirst);
                    Picasso.get().load(iconList.get(1)).into(imageSecond);
                    Picasso.get().load(iconList.get(2)).into(imageThird);
                    Picasso.get().load(iconList.get(3)).into(imageFourth);
                    Picasso.get().load(iconList.get(4)).into(imageFifth);*/

                    Log.d("Weather", weekDays.toString());
                }
            }

            @Override
            public void onFailure(Call<WeatherAdvanced> call, Throwable t) {

                Log.d("Weather", t.getLocalizedMessage());
            }
        });

        return view;
    }

    private void getCurrentTime() {

        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = calendar.getTimeZone();

        Date date = Calendar.getInstance(TimeZone.getDefault()).getTime();

        long millis = date.getTime();
        millis = millis + timeZone.getOffset(millis);

        currentTime = (millis / 1000L);
    }
}
