package com.shellming.sparkathon.fragment;

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
import com.shellming.sparkathon.model.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            }

        }
    }

    private void setupRecylerView(){
        try {
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new TendayViewAdaper(new ArrayList<Weather>());
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
}
