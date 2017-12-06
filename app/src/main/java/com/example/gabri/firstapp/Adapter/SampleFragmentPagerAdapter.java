package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.gabri.firstapp.FragmentPage1;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Platform;

import java.util.ArrayList;
import java.util.List;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private int PAGE_COUNT = 1;
    private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
    private Context context;

    private List<Object> objectList;

    private ArrayList<Fragment> listFragment;
    private List<Platform> listPlatform;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, List<Object> objectList) {
        super(fm);
        this.context = context;
        listFragment= new ArrayList<>();
        this.objectList=objectList;
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
            case 0:FragmentPage1 f1= new FragmentPage1();
            f1.setList(objectList);
            current=f1;
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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.listPlatform = Data.getData().getListPlatform();
        //DA COMPLETARE
    }
}