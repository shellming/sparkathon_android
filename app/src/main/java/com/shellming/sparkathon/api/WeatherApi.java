package com.shellming.sparkathon.api;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.shellming.sparkathon.model.MyMessage;
import com.shellming.sparkathon.model.Weather;
import com.shellming.sparkathon.model.WeatherForecastDaily;
import com.shellming.sparkathon.model.WeatherForecastHourly;
import com.shellming.sparkathon.model.WeatherObservationCurrent;
import com.shellming.sparkathon.model.WeatherResponse;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Challenge;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * Created by ruluo1992 on 11/30/2015.
 */
public class WeatherApi {
    public static String URL_TEMPLATE = "";
    private static String CURRENT = "/v2/observations/current";
    private static String TEN_DAYS_LATER = "/v2/forecast/daily/10day";
    private static String ONE_DAY_LATER = "/v2/forecast/hourly/24hour";
    public static String WEATHER_USER = "";
    public static String WEATHER_PASS = "";  // read from conf.properties when main activity created

    private static OkHttpClient client = new OkHttpClient();

    private static void getWeather(String latitude, String longitude, Class responseType, MyMessage.Type type){
        String url = null;
        switch (type){
            case OBSERVATION_CURRENT:
                url = String.format(URL_TEMPLATE, CURRENT, latitude, longitude);
                break;
            case FORCAST_10DAY:
                url = String.format(URL_TEMPLATE, TEN_DAYS_LATER, latitude, longitude);
                break;
            case FORCAST_24HOUR:
                url = String.format(URL_TEMPLATE, ONE_DAY_LATER, latitude, longitude);
                break;
        }
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setReadTimeout(15, TimeUnit.SECONDS);
        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credential = Credentials.basic(WEATHER_USER, WEATHER_PASS);
                return response.request().newBuilder().header("Authorization", credential).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }
        });

        Request request = new Request.Builder().url(url).build();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!weather url:" + url);
        WeatherApiCallback callback = new WeatherApiCallback(type, responseType);
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void getTendayForecast(String latitude, String longitude){
        getWeather(latitude, longitude, WeatherForecastDaily.class, MyMessage.Type.FORCAST_10DAY);
    }

    public static void getCurrentWeather(String latitude, String longitude){
        getWeather(latitude, longitude, WeatherObservationCurrent.class, MyMessage.Type.OBSERVATION_CURRENT);
    }

    public static void get24HoursForecast(String latitude, String longitude){
        getWeather(latitude, longitude, WeatherForecastHourly.class, MyMessage.Type.FORCAST_24HOUR);
    }
}

class WeatherApiCallback implements Callback {
    private Class entityClass;
    private MyMessage.Type type;

    public WeatherApiCallback(MyMessage.Type type, Class entityClass) {
        this.type = type;
        this.entityClass = entityClass;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        MyMessage msg = new MyMessage();
        msg.what = 1;
        msg.type = type;
        EventBus.getDefault().post(msg);
        e.printStackTrace();
    }

    @Override
    public void onResponse(Response response) throws IOException {
        String responseStr =  response.body().string();

        System.out.println(response.request().url());
        Gson gson = new Gson();
        WeatherResponse wResponse = (WeatherResponse) gson.fromJson(responseStr, entityClass);
        List<Weather> weathers = wResponse.getWeatherData();
        MyMessage msg = new MyMessage();
        msg.what = 0;
        msg.data = weathers;
        msg.type = type;
        EventBus.getDefault().post(msg);
    }
}


