package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.gabri.firstapp.FragmentPage1;

import java.util.ArrayList;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private int PAGE_COUNT = 1;
    private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
    private Context context;

    private ArrayList<Fragment> listFragment;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        listFragment= new ArrayList<>();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment current;
        System.out.println("--------------------------LA POSIZIONE E'"+ position);
        switch (position){
            case 0: current= new FragmentPage1();
            listFragment.add(current);
            return current;
        }
            return null;
        //return PageFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}