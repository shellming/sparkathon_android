package com.shellming.sparkathon.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ruluo1992 on 11/3/2015.
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter  {

    private List<Fragment> mFragments;
    private List<String> mTitles;
//    private int index = 0;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

//    public void setIndex(int index){
//        this.index = index;
//        System.out.print("!!!!!!!!!!!!! new index:" + index);
//        notifyDataSetChanged();
//    }

//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
