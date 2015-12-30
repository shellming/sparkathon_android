package com.shellming.sparkathon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.shellming.sparkathon.R;
import com.shellming.sparkathon.model.MyMessage;
import com.shellming.sparkathon.model.Weather;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ruluo1992 on 12/10/2015.
 */
public class OneDayForcastFragment extends Fragment {
    private LineChart temperatureChart;
    private LineChart windChart;
    private LineChart wetChart;
    private LineChart visitChart;
    private LineChart pressureChart;
    private View mView;

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

    public void onEventMainThread(MyMessage message) {
        if(message.type == MyMessage.Type.FORCAST_24HOUR){
            List<Weather> weathers = (List<Weather>) message.data;
            List<String> labels = new ArrayList<>();

            List<Entry> temperatureEnties = new ArrayList<>();
            List<Entry> windEnties = new ArrayList<>();
            List<Entry> wetEnties = new ArrayList<>();
            List<Entry> visitEnties = new ArrayList<>();
            List<Entry> pressureEnties = new ArrayList<>();

            for(int i = 0; i < weathers.size(); i++){
                Weather weather = weathers.get(i);
                int x = Integer.valueOf(weather.getFormatDate());
                float temperature = Float.valueOf(weather.getTemp());
                float windSpeed = Float.valueOf(weather.getWindSpeed());
                float wet = Float.valueOf(weather.getWet());
                float visit = Float.valueOf(weather.getVisit().toString());
                float pressure = Float.valueOf(weather.getPressure().toString());

                Entry entry = new Entry(temperature, i);
                temperatureEnties.add(entry);

                entry = new Entry(windSpeed, i);
                windEnties.add(entry);

                entry = new Entry(wet, i);
                wetEnties.add(entry);

                entry = new Entry(visit, i);
                visitEnties.add(entry);

                entry = new Entry(pressure, i);
                pressureEnties.add(entry);

                labels.add(String.valueOf(x));
            }

            setData("Temperature", temperatureEnties, labels, temperatureChart);
            setData("WindSpeed", windEnties, labels, windChart);
            setData("Humidity", wetEnties, labels, wetChart);
            setData("Visibility", visitEnties, labels, visitChart);
            setData("Pressure", pressureEnties, labels, pressureChart);
        }
    }

    private void setData(String title, List<Entry> entries, List<String> labels, LineChart chart){
        LineDataSet dataSet = new LineDataSet(entries, title);
        dataSet.setLineWidth(5.0f);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawValues(false);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        LineData data = new LineData(labels, dataSet);
        data.addDataSet(dataSet);
        chart.setData(data);
        chart.invalidate();
    }

    private void setupChart(LineChart chart){
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
//        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setStartAtZero(false);

        yAxis = chart.getAxisRight();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);

        chart.setDescription("");
        chart.setDrawGridBackground(false);
        chart.getLegend().setEnabled(false);
        chart.setClickable(false);
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_oneday, container, false);
        temperatureChart = (LineChart) mView.findViewById(R.id.temperature_chart);
        windChart = (LineChart) mView.findViewById(R.id.wind_chart);
        wetChart = (LineChart) mView.findViewById(R.id.wet_chart);
        visitChart = (LineChart) mView.findViewById(R.id.visit_chart);
        pressureChart = (LineChart) mView.findViewById(R.id.pressure_chart);

//        String[] xLabels = new String[24];
//        for(int i = 0; i < 24; i++){
//            xLabels[i] = String.valueOf(i);
//        }
//
//        LineData data = new LineData(xLabels);
//        temperatureChart.setData(data);
//        windChart.setData(data);

        setupChart(temperatureChart);
        setupChart(windChart);
        setupChart(wetChart);
        setupChart(visitChart);
        setupChart(pressureChart);

        return mView;
    }
}
