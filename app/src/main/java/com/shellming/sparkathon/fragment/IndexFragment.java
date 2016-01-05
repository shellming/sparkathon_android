package com.shellming.sparkathon.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shellming.sparkathon.R;
import com.shellming.sparkathon.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruluo1992 on 11/27/2015.
 */
public class IndexFragment extends Fragment {
    private MyViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_index, container, false);

        ViewPager viewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
//
        FloatingActionsMenu actionsMenu = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions);
        actionsMenu.setVisibility(View.VISIBLE);

        return mView;
    }

    private void setupViewPager(ViewPager viewPager) {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        fragments.add(new CurrentObservationFragment());
        fragments.add(new OneDayForcastFragment());
        fragments.add(new TenDayForcastFragment());

        titles.add("current");
        titles.add("24hours");
        titles.add("10days");

        viewPagerAdapter = new MyViewPagerAdapter(getFragmentManager(), fragments, titles);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int lastPosition  = 0;
            private FloatingActionsMenu fb;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position != lastPosition){
                    if(fb == null)
                        fb = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions);
                    if(position == 0){
                        show(fb);
                    }
                    else{
                        hide(fb);
                    }
                    lastPosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            private void hide(final View menu){
                menu.animate()
                        .translationY(menu.getHeight())
                        .setDuration(300);
            }

            private void show(final View menu){
                menu.animate()
                        .translationY(0)
                        .setDuration(300);
            }
        });
    }
}
