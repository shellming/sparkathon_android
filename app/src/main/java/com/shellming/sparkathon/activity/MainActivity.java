package com.shellming.sparkathon.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.shellming.sparkathon.R;
import com.shellming.sparkathon.adapter.MyViewPagerAdapter;
import com.shellming.sparkathon.api.WeatherApi;
import com.shellming.sparkathon.constant.GlobalConstant;
import com.shellming.sparkathon.fragment.IndexFragment;
import com.shellming.sparkathon.fragment.OneDayForcastFragment;
import com.shellming.sparkathon.fragment.TenDayForcastFragment;
import com.shellming.sparkathon.fragment.TimelineFragment;
import com.shellming.sparkathon.model.MyMessage;
import com.shellming.sparkathon.util.LocationUtil;
import com.shellming.sparkathon.util.TwitterUtil;
import com.shellming.sparkathon.util.UserUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private int currentSelected;

//    private Map<Integer, Integer> menuId2Index;

    private CircleImageView mAvatar;
    private TextView mUsername;
    private TextView mUserinfo;
    private TextView mUserExtra;

    public void onEventMainThread(MyMessage message) {
        if(message.type == MyMessage.Type.GET_CITY_NAME) {
            if(message.what == MyMessage.SUCCESS){
                String city = message.data.toString();
                setTitle(city);
            }
            else{
                setTitle("Error");
            }
        }
    }

    private void refreshUser(User user){
        TwitterStream twitterStream = TwitterUtil.getInstance().getTwitterStream();
        if(user != null) {
            String avatar = user.getProfileImageURL();
            String username = user.getName();
            String sign = user.getDescription();

            ImageLoader.getInstance().displayImage(avatar, mAvatar);
            mUsername.setText(username);
            mUserinfo.setText(sign);
            mUserExtra.setText("Touch avatar to logout");
            try {
                AccessToken token = UserUtil.getAccessToken(getApplicationContext());
                twitterStream.setOAuthAccessToken(token);
                twitterStream.addListener(TwitterUtil.listener);
                twitterStream.user();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            mUsername.setText(GlobalConstant.DEFAULT_USERNAME);
            mAvatar.setImageResource(GlobalConstant.DEFAULT_AVATAR);
            mUserinfo.setText("");
            mUserExtra.setText("");
            twitterStream.removeListener(TwitterUtil.listener);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        processCallback(intent);
    }


    private void processCallback(Intent intent){
        Uri uri = intent.getData();
        if(uri != null && uri.toString().startsWith(GlobalConstant.TWITTER_CALLBACK_URL)){
            String oauthVerifier = uri.getQueryParameter("oauth_verifier");
            RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
            String oauthToken = uri.getQueryParameter("oauth_token");
            System.out.println("!!!!!!!!!!!!! oauthverifier:" + oauthVerifier);
            new TwitterGetAccessTokenTask().execute(oauthVerifier);
        }
    }

    private void init(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        try {
            InputStream propertyStream = getAssets().open("conf.properties");
            Properties properties = new Properties();
            properties.load(propertyStream);
            WeatherApi.WEATHER_USER = properties.getProperty("weather.username");
            WeatherApi.WEATHER_PASS = properties.getProperty("weather.password");
            WeatherApi.URL_TEMPLATE = properties.getProperty("weather.url");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! url" + WeatherApi.URL_TEMPLATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        init();

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! enter activity");
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setupToolbar();
        setupDrawerContent();

        currentSelected = R.id.index;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new IndexFragment());
        transaction.commit();

        if (UserUtil.isLogin(getApplicationContext())) {
            new TwitterGetUserInfoTask().execute();
        }

        LocationUtil.getLocation(getApplicationContext());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! leave activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupDrawerContent() {
        currentSelected = R.id.index;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nagivation);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        if(menuItem.getItemId() != currentSelected) {
                            currentSelected = menuItem.getItemId();
                            Fragment fragment = null;
                            switch (currentSelected){
                                case R.id.index:
                                    fragment = new IndexFragment();
                                    break;
                                case R.id.timeline:
                                    fragment = new TimelineFragment();
                                    break;
                                case R.id.notifaction:
                                    break;
                            }
                            if(fragment != null){
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, fragment);
                                transaction.commit();
                                System.out.println("!!!!!!!!!!!!!! fragment commit:" + fragment);
                            }
//                            viewPagerAdapter.setIndex(currentSelected);
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        View headerView = navigationView.getHeaderView(0);
//        View headerView = findViewById(R.id.header);
        mAvatar = (CircleImageView) headerView.findViewById(R.id.profile_image);
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserUtil.isLogin(getApplicationContext())){
                    new TwitterAuthenticateTask().execute();
                }
                else{
                    refreshUser(null);
                    UserUtil.logout(getApplicationContext());
                }
            }
        });
        mUsername = (TextView) headerView.findViewById(R.id.username);
        mUserinfo = (TextView) headerView.findViewById(R.id.userinfo);
        mUserExtra = (TextView) headerView.findViewById(R.id.extra);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class TwitterAuthenticateTask extends AsyncTask<String,Integer, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
            System.out.println("!!!!!!!!!!!!!!!!!!!! " + requestToken.getAuthenticationURL());
            startActivity(intent);
        }

        @Override
        protected RequestToken doInBackground(String[] params) {
            return TwitterUtil.getInstance().getRequestToken();
        }
    }

    class TwitterGetAccessTokenTask extends AsyncTask<String,Integer, User>{

        @Override
        protected void onPostExecute(User user) {
            MainActivity.this.refreshUser(user);
        }

        @Override
        protected User doInBackground(String[] params) {
            Twitter twitter = TwitterUtil.getInstance().getTwitter();
            RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
                UserUtil.login(getApplicationContext(), accessToken.getToken(), accessToken.getTokenSecret());
                twitter.setOAuthAccessToken(accessToken);
                return twitter.showUser(accessToken.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class TwitterGetUserInfoTask extends AsyncTask<String, Integer, User>{

        @Override
        protected void onPostExecute(User user) {
            System.out.println("//!!!!!!!!!!!!!!!!!!! get user" + user);
            MainActivity.this.refreshUser(user);
        }

        @Override
        protected User doInBackground(String... params) {
            AccessToken token = UserUtil.getAccessToken(getApplicationContext());
            if(token != null){
                Twitter twitter = TwitterUtil.getInstance().getTwitter();
                twitter.setOAuthAccessToken(token);
                try {
                    return twitter.showUser(token.getUserId());
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
