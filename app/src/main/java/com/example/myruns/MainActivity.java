package com.example.myruns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Start start;
    History history;
    Settings settings;

    ArrayList<Fragment> tabs;

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.start = new Start();
        this.history = new History();
        this.settings = new Settings();

        this.tabs = new ArrayList<>();

        this.tabs.add(start);
        this.tabs.add(history);
        this.tabs.add(settings);

        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.tabLayout = (TabLayout) findViewById(R.id.tab);

        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getSupportFragmentManager(), this.tabs);

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}