package com.shellming.sparkathon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shellming.sparkathon.R;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.Status;

/**
 * Created by ruluo1992 on 12/24/2015.
 */
public class TimelineRecyclerViewAdapter extends RecyclerView.Adapter<TimelineRecyclerViewAdapter.ViewHolder> {

    private List data;

    public TimelineRecyclerViewAdapter(List data) {
        this.data = data;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView location;
        public TextView time;
        public TextView like;
        public CircleImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            location = (TextView) itemView.findViewById(R.id.position);
            like = (TextView) itemView.findViewById(R.id.run_like);
            time = (TextView) itemView.findViewById(R.id.run_time);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
        }
    }

    public void setData(List data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public TimelineRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TimelineRecyclerViewAdapter.ViewHolder holder, int position) {
        Status status = (Status) data.get(position);
        String content = status.getText();
        holder.location.setText(content);
        holder.time.setText(content);
        holder.like.setText(String.valueOf(status.getFavoriteCount()));
        ImageLoader.getInstance().displayImage(status.getUser().getProfileImageURL(), holder.avatar);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
