package com.example.myruns;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentPageAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;

    public FragmentPageAdapter(FragmentManager f, ArrayList<Fragment> list) {
        super(f, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = list;
    }

    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        if(position == 0)
            return "Start";
        else if(position == 1)
            return "History";
        else if(position == 2)
            return "Settings";
        else
            return null;
    }
}
