package com.pufei.gxdt.module.user.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FavoriteTabVpAdapter extends FragmentPagerAdapter {
    private List<Fragment> mData;
    private List<String> mTitle;

    public FavoriteTabVpAdapter(FragmentManager fm, List<Fragment> mData, List<String> mTitle) {
        super(fm);
        this.mData = mData;
        this.mTitle = mTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }
}
