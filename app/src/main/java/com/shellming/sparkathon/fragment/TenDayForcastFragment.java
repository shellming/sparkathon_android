package com.shellming.sparkathon.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.shellming.sparkathon.R;
import com.shellming.sparkathon.adapter.RecylerViewAdapter;
import com.shellming.sparkathon.adapter.TendayViewAdaper;
import com.shellming.sparkathon.api.WeatherApi;
import com.shellming.sparkathon.constant.GlobalConstant;
import com.shellming.sparkathon.model.MyMessage;
import com.shellming.sparkathon.model.RunExpModel;
import com.shellming.sparkathon.model.Weather;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * Created by ruluo1992 on 12/10/2015.
 */
public class TenDayForcastFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TendayViewAdaper adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView =inflater.inflate(R.layout.fragment_tenday, container, false);

        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        setupRecylerView();

        return mView;
    }

    public void onEventMainThread(MyMessage message) {
        if(message.type == MyMessage.Type.FORCAST_10DAY){
            if(message.what == MyMessage.SUCCESS){
                List<Weather> data = (List<Weather>) message.data;
                data.remove(0);
                adapter.setData(data);
//                new AnalysisRunExpTask().execute(data.toArray(new Weather[0]));
            }

        }
    }

    private void setupRecylerView(){
        try {
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new TendayViewAdaper(new ArrayList<Weather>(), getContext());
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class AnalysisRunExpTask extends AsyncTask<Weather, Integer, String>{

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            List<Double> result = gson.fromJson(s, List.class);
            adapter.setRunExps(result);
        }

        @Override
        protected String doInBackground(Weather... params) {
            try {
                List<RunExpModel> models = new ArrayList<>();
                for(Weather weather : params){
                    models.add(RunExpModel.fromWeather(weather));
                }
                Gson gson = new Gson();
                String content = gson.toJson(models);
                OkHttpClient client = new OkHttpClient();
                client.setConnectTimeout(15, TimeUnit.SECONDS);
                client.setReadTimeout(15, TimeUnit.SECONDS);

                String url = GlobalConstant.RUN_EXP_URL;
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(JSON, content);
                Request request = new Request.Builder().url(url).post(requestBody).build();
                Call call = client.newCall(request);

                Response response = call.execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }
}
