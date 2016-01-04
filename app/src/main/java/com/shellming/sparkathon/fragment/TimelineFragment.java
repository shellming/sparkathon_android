package com.shellming.sparkathon.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shellming.sparkathon.R;
import com.shellming.sparkathon.adapter.TimelineRecyclerViewAdapter;
import com.shellming.sparkathon.constant.GlobalConstant;
import com.shellming.sparkathon.model.TwitterModel;
import com.shellming.sparkathon.util.ToastUtil;
import com.shellming.sparkathon.util.TwitterUtil;
import com.shellming.sparkathon.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by ruluo1992 on 12/24/2015.
 */
public class TimelineFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private TimelineRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_timeline, container, false);

        recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.timeline_list);
        adapter = new TimelineRecyclerViewAdapter(new ArrayList());
        setupSwipeRefreshLayout();
        setupRecylerView();

        FloatingActionsMenu actionsMenu = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions);
        actionsMenu.setVisibility(View.INVISIBLE);

        return swipeRefreshLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    private void setupSwipeRefreshLayout(){
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!! is refreshing");
                new TimelineRefreshTask().execute(GlobalConstant.TWITTER_TAG);
            }
        });
    }

    private void setupRecylerView(){
        try {
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            new TimelineRefreshTask().execute(GlobalConstant.TWITTER_TAG);
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    int visibleItemCount = linearLayoutManager.getChildCount();
//                    int totalItemCount = linearLayoutManager.getItemCount();
//                    int pastItems = linearLayoutManager.findFirstVisibleItemPosition();
//
//                    if (!onLoading) {
//                        if ((pastItems + visibleItemCount) >= totalItemCount) {
//                            ToastUtil.showToast(getContext(), "loading...", Toast.LENGTH_LONG);
//                            onLoading = true;
//                            currentPage++;
//                        }
//                    }
//                }
//            });
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TimelineRefreshTask extends AsyncTask<String, Integer, List>{

        @Override
        protected void onPostExecute(List list) {
            if(swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
//            if(onLoading) {
//                onLoading = false;
//                ToastUtil.showToast(getContext(), "loading done", Toast.LENGTH_SHORT);
//            }
            if(list == null){
                ToastUtil.showToast(getContext(), "get timeline error", Toast.LENGTH_SHORT);
                return;
            }
            System.out.println("!!!!!!!!!!!!!!!!!!! timeline size:" + list.size());
            adapter.setData(list);
        }

        @Override
        protected List doInBackground(String... params) {
            if(!UserUtil.isLogin(getContext()))
                return new ArrayList();
            Query query = new Query(params[0]);
//            query.since("2015-12-01");
//            Double latitude = Double.valueOf(GlobalConstant.latitude);
//            Double longitude = Double.valueOf(GlobalConstant.logitude);
//            GeoLocation location = new GeoLocation(latitude, longitude);
//            query.setGeoCode(location, 5, Query.Unit.km);
            Twitter twitter = TwitterUtil.getInstance().getTwitter();
            try {
                QueryResult result = twitter.search(query);
                System.out.println("get since" + query.getSince());
                List<twitter4j.Status> statuses = result.getTweets();
                List<TwitterModel> models = new ArrayList<>();
                for(int i = 0; i < statuses.size(); i++){
                    TwitterModel model = TwitterModel.fromTwitter(statuses.get(i));
                    if(model != null)
                        models.add(model);
                }
                return models;
            } catch (TwitterException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
