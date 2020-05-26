package com.manitosdev.gcatcast.ui.main.features.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.manitosdev.gcatcast.R;
import com.manitosdev.gcatcast.ui.main.features.discovery.DiscoveryFragment;
import com.manitosdev.gcatcast.ui.main.features.popular.PopularFragment;
import com.manitosdev.gcatcast.ui.main.features.subscribed.SubscribedFragment;

public class MainSectionPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
        R.string.main_tab_popular, R.string.main_tab_discovery, R.string.main_tab_subscribed
    };
    private final Context mContext;

    public MainSectionPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case R.string.main_tab_discovery:
                return DiscoveryFragment.newInstance();
            case R.string.main_tab_subscribed:
                return SubscribedFragment.newInstance();
            default:
                return PopularFragment.newInstance();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() { return TAB_TITLES.length; }

    @Override
    public long getItemId(int position) {
        return TAB_TITLES[position];
    }
}