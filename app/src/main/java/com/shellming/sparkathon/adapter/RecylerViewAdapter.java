package com.shellming.sparkathon.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shellming.sparkathon.R;

import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 11/3/2015.
 */
public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder> {
    private List<Map> data;

    private int lastPosition = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private List<Map> data;

        public final View mView;

        public final TextView textView;
        public final ImageView imgView;

        public ViewHolder(View view, List<Map> data) {
            super(view);
            mView = view;
            textView = (TextView) view.findViewById(R.id.list_item_text);
            imgView = (ImageView) view.findViewById(R.id.list_item_img);

            this.data = data;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mView.toString();
        }


        public void setContent(final int position){
            try {
                Map<String, Object> item = data.get(position);
                Integer icon = (Integer) item.get("icon");
                String content = (String) item.get("content");
                textView.setText(content);
                imgView.setImageResource(icon);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public RecylerViewAdapter(List<Map> data){
        this.data = data;
    }

    public void addData(List<Map> newData){
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public void setData(List<Map> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_detail, parent, false);
        return new ViewHolder(view, data);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(position);
        setAnimation(holder.mView, position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.fade_in);
        animation.setStartOffset(100 * position);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
