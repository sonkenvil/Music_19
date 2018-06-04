package com.framgia.nguyenson.music_19.screen.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        TextView.OnEditorActionListener {
    private ImageView mImageMenu, mImageSearch;
    private EditText mEditSeachMusic;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mTabLayout.setupWithViewPager(mViewPager);
        setupViewPager();
        setOnListener();
    }

    private void setOnListener() {
        mImageMenu.setOnClickListener(this);
        mImageSearch.setOnClickListener(this);
        mEditSeachMusic.setOnEditorActionListener(this);
    }

    private void initViews() {
        mImageMenu = findViewById(R.id.image_menu);
        mImageSearch = findViewById(R.id.image_search);
        mEditSeachMusic = findViewById(R.id.edit_search_music);
        mTabLayout = findViewById(R.id.tab_home);
        mViewPager = findViewById(R.id.viewpager_home);
    }

    private void setupViewPager() {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < Constants.GENRE.length; i++) {
            pagerAdapter.addFragment(new Fragment(), Constants.GENRE[i]);
        }
        mViewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
