package com.framgia.nguyenson.music_19.screen.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mListFragment;
    private List<String> mTitle;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        mListFragment = new ArrayList<>();
        mTitle = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title) {
        mListFragment.add(fragment);
        mTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }
}
