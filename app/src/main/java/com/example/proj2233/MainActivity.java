package com.example.proj2233;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView cityTextView;
    TextView weatherTextView;
    TextView tempTextView;
    TextView humidityTextView;
    TextView cloudsTextView;
    TextView windTextView;

    String city = "Minsk";
    String weather;
    int temperature;
    int humidity;
    int windDegrees;
    int windSpeed;
    int cloudsPercent;
    String windDirection = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTextView = (TextView)findViewById(R.id.main_city_tv);
        weatherTextView = (TextView)findViewById(R.id.main_weather_tv);
        tempTextView = (TextView)findViewById(R.id.main_temperature_tv);
        humidityTextView = (TextView)findViewById(R.id.main_humidity_tv);
        cloudsTextView = (TextView)findViewById(R.id.main_clouds_tv);
        windTextView = (TextView)findViewById(R.id.main_wind_tv);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://community-open-weather-map.p.rapidapi.com/weather?q=Minsk%2Cby&units=metric&mode=json")
                        .get()
                        .addHeader("x-rapidapi-key", "7a9f99123dmsh55196b62bfb2889p1af16cjsn3e36176afef1")
                        .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        if (jsonResponse.getInt("cod") == 200) {
                            weather = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("main");
                            temperature = jsonResponse.getJSONObject("main").getInt("temp");
                            humidity = jsonResponse.getJSONObject("main").getInt("humidity");
                            windDegrees = jsonResponse.getJSONObject("wind").getInt("deg");
                            windSpeed = jsonResponse.getJSONObject("wind").getInt("speed");
                            cloudsPercent = jsonResponse.getJSONObject("clouds").getInt("all");

                            if (windDegrees > 337 || windDegrees <= 22)
                                windDirection = "N";
                            if (windDegrees > 22 && windDegrees <= 67)
                                windDirection = "NE";
                            if (windDegrees > 67 && windDegrees <= 112)
                                windDirection = "E";
                            if (windDegrees > 112 && windDegrees <= 157)
                                windDirection = "SE";
                            if (windDegrees > 157 && windDegrees <= 202)
                                windDirection = "S";
                            if (windDegrees > 202 && windDegrees <= 247)
                                windDirection = "SW";
                            if (windDegrees > 247 && windDegrees <= 292)
                                windDirection = "W";
                            if (windDegrees > 292 && windDegrees <= 337)
                                windDirection = "NW";

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cityTextView.setText(city);
                                    weatherTextView.setText(weather);
                                    tempTextView.setText((String) (temperature + "°C"));
                                    humidityTextView.setText((String) (humidity + "%"));
                                    cloudsTextView.setText((String) (cloudsPercent + "%"));
                                    windTextView.setText((String) (windDirection + ", " + windSpeed + " m/s"));
                                }
                            });
                        }

                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}