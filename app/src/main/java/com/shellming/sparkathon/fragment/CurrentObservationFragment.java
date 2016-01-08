package com.shellming.sparkathon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shellming.sparkathon.R;
import com.shellming.sparkathon.adapter.RecylerViewAdapter;
import com.shellming.sparkathon.api.WeatherApi;
import com.shellming.sparkathon.constant.GlobalConstant;
import com.shellming.sparkathon.model.MyMessage;
import com.shellming.sparkathon.model.Weather;
import com.shellming.sparkathon.util.LocationUtil;
import com.shellming.sparkathon.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by ruluo1992 on 12/12/2015.
 */
public class CurrentObservationFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecylerViewAdapter adapter;

    private TextView mTempView;
    private TextView mDesView;
    private ImageView mWeatherImgView;
    private TextView mDateTime;
    private TextView mDegreeView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_current, container, false);
        recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.recyclerview);
        mTempView = (TextView) swipeRefreshLayout.findViewById(R.id.tempture);
        mDesView = (TextView) swipeRefreshLayout.findViewById(R.id.description);
        mWeatherImgView = (ImageView) swipeRefreshLayout.findViewById(R.id.weatherImg);
        mDateTime = (TextView) swipeRefreshLayout.findViewById(R.id.data_time);
        mDegreeView = (TextView) swipeRefreshLayout.findViewById(R.id.degree);

        FloatingActionsMenu actionsMenu = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions);

        setupActionButton(actionsMenu);
        setupRecylerView();
        setupSwipeRefreshLayout();

        WeatherApi.getCurrentWeather(GlobalConstant.latitude, GlobalConstant.logitude);
        WeatherApi.getTendayForecast(GlobalConstant.latitude, GlobalConstant.logitude);
        WeatherApi.get24HoursForecast(GlobalConstant.latitude, GlobalConstant.logitude);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        return swipeRefreshLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setupActionButton(final FloatingActionsMenu menu){
        FloatingActionButton rightBtn = (FloatingActionButton) menu.findViewById(R.id.action_right);
        FloatingActionButton wrongBtn = (FloatingActionButton) menu.findViewById(R.id.action_wrong);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(getContext(), "Thanks for your feedback!", Toast.LENGTH_SHORT);
                menu.collapseImmediately();
            }
        });
        wrongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(getContext(), "Thanks for your feedback!", Toast.LENGTH_SHORT);
                menu.collapseImmediately();
            }
        });
    }

    private void setupRecylerView(){
        try {
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new RecylerViewAdapter(new ArrayList<Map>());
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSwipeRefreshLayout(){
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                LocationUtil.getLocation(getContext());
            }
        });
    }

    public void onEventMainThread(MyMessage message) {
        if(message.type == MyMessage.Type.OBSERVATION_CURRENT) {
            if(message.what == 0){
                List<Weather> weathers = (List<Weather>) message.data;
                Weather weather = weathers.get(0);
                List<Map> data = formatWeatherData(weathers);
                adapter.setData(data);
                System.out.println(weather.getTemp());
                mTempView.setText(weather.getTemp().toString());
                mDesView.setText(weather.getWeatherDesc());
                mWeatherImgView.setImageResource(weather.getIcon());
                mDateTime.setText(weather.getFormatDate());
                mDegreeView.setText("℃");
            }else {
                ToastUtil.showToast(getContext(), "刷新失败", Toast.LENGTH_SHORT);
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private List<Map> formatWeatherData(List<Weather> weathers){
        Weather weather = weathers.get(0);

        List<Map> data = new ArrayList<>();
        Map huminity = new HashMap();
        huminity.put("icon", R.drawable.icon_wet);
        huminity.put("content", weather.getWet() + "%");

        Map windSpeed = new HashMap();
        windSpeed.put("icon", R.drawable.icon_windspeed);
        windSpeed.put("content", weather.getWindSpeed() + " km/h");

        Map visit = new HashMap();
        visit.put("icon", R.drawable.icon_visit);
        visit.put("content", weather.getVisit().toString());

        Map sunrise = new HashMap();
        sunrise.put("icon", R.drawable.icon_sunrise);
        String sunriseStr = weather.getSunrise();
        int start = sunriseStr.indexOf('T');
        int end = sunriseStr.indexOf('+');
        sunriseStr = sunriseStr.substring(start + 1, end);
        sunrise.put("content", sunriseStr);

        Map sunset = new HashMap();
        sunset.put("icon", R.drawable.icon_sunset);
        String sunsetStr = weather.getSunset();
        sunsetStr = sunsetStr.substring(start + 1, end);
        sunset.put("content", sunsetStr);

        Map pressure = new HashMap();
        pressure.put("icon", R.drawable.icon_pressure);
        pressure.put("content", weather.getPressure() + " mbar");

        data.add(huminity);
        data.add(windSpeed);
        data.add(visit);
        data.add(sunrise);
        data.add(sunset);
        data.add(pressure);

        return data;
    }
}
