package com.shellming.sparkathon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shellming.sparkathon.R;
import com.shellming.sparkathon.model.Weather;

import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 12/11/2015.
 */
public class TendayViewAdaper extends RecyclerView.Adapter<TendayViewAdaper.ViewHolder> {
    private List<Weather> data;

    public TendayViewAdaper(List<Weather> data) {
        this.data = data;
    }

    @Override
    public TendayViewAdaper.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_card, parent, false);
        return new ViewHolder(view, data);
    }

    @Override
    public void onBindViewHolder(TendayViewAdaper.ViewHolder holder, int position) {
        Weather weather = data.get(position);
        holder.mTempView.setText(weather.getTemp().toString());
        holder.mDesView.setText(weather.getWeatherDesc());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!desc:" + weather.getWeatherDesc());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!sunrise:" + weather.getSunrise());
        holder.mWeatherImgView.setImageResource(weather.getIcon());
        holder.mDateTime.setText(weather.getFormatDate());
        holder.mDegreeView.setText("℃");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Weather> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private List<Weather> data;
        public final View mView;

        public TextView mTempView;
        public TextView mDesView;
        public ImageView mWeatherImgView;
        public TextView mDateTime;
        public TextView mDegreeView;

        public ViewHolder(View view, List<Weather> data) {
            super(view);
            mView = view;
            this.data = data;

            mTempView = (TextView) mView.findViewById(R.id.tempture);
            mDesView = (TextView) mView.findViewById(R.id.description);
            mWeatherImgView = (ImageView) mView.findViewById(R.id.weatherImg);
            mDateTime = (TextView) mView.findViewById(R.id.data_time);
            mDegreeView = (TextView) mView.findViewById(R.id.degree);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mView.toString();
        }

    }
}
