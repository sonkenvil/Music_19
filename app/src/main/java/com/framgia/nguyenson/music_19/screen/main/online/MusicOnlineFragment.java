package com.framgia.nguyenson.music_19.screen.main.online;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.screen.main.PagerAdapter;
import com.framgia.nguyenson.music_19.screen.main.genres.GenresFragment;
import com.framgia.nguyenson.music_19.utils.Constants;

public class MusicOnlineFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_music_online, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTabLayout = getView().findViewById(R.id.tab_home);
        mViewPager = getView().findViewById(R.id.viewpager_home);
        mTabLayout.setupWithViewPager(mViewPager);
        setupViewPager();
    }

    private void setupViewPager() {
        PagerAdapter pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        for (int i = 0; i < Constants.GenreBase.GENRE.length; i++) {
            pagerAdapter.addFragment(GenresFragment.newInstance(Constants.GenreBase.GENRE[i]),
                    Constants.GenreBase.GENRE[i]);
        }
        mViewPager.setAdapter(pagerAdapter);
    }
}
