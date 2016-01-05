package com.shellming.sparkathon.api;

import com.google.gson.Gson;
import com.shellming.sparkathon.model.MyMessage;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * Created by ruluo1992 on 12/10/2015.
 */
public class GoogleMapApi {
    private static final String CITY_URL_TEMPLATE = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&sensor=true";
    private static OkHttpClient client = new OkHttpClient();

    public static void getCityName(String latitude, String longitude){
        String url = String.format(CITY_URL_TEMPLATE, latitude, longitude);
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setReadTimeout(15, TimeUnit.SECONDS);

        Request request = new Request.Builder().url(url).build();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!location url:" + url);
        Callback callback = new GoogleMapApiCallback();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}

class GoogleMapApiCallback implements Callback {

    @Override
    public void onFailure(Request request, IOException e) {
        MyMessage msg = new MyMessage();
        msg.what = 1;
        msg.type = MyMessage.Type.GET_CITY_NAME;
        EventBus.getDefault().post(msg);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        String responseStr =  response.body().string();

        System.out.println(response.request().url());
        Gson gson = new Gson();
        Map map = gson.fromJson(responseStr, HashMap.class);

        List<Map> results = (List<Map>) map.get("results");
        Map result = results.get(0);
        String addr = result.get("formatted_address").toString();
        String[] parts = addr.split(",");

        String city = "";
        if(parts.length >= 3)
            city = parts[parts.length - 3];
        else
            city = parts[parts.length - 1];

        MyMessage message = new MyMessage();
        message.data = city;
        message.type = MyMessage.Type.GET_CITY_NAME;
        message.what = 0;

        EventBus.getDefault().post(message);
    }
}
