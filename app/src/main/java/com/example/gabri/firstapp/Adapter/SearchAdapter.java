package com.example.gabri.firstapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.gabri.firstapp.UserTabFragment;
import com.example.gabri.firstapp.View.GameTabFragment;

/**
 * Created by Gabri on 04/02/18.
 */

public class SearchAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private UserTabFragment userTab;
    private GameTabFragment gameTab;

    public SearchAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.gameTab=new GameTabFragment();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                this.userTab = new UserTabFragment();
                return userTab;
            case 1:
                return this.gameTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public UserTabFragment getUserTab(){
        return this.userTab;
    }

    public GameTabFragment getGameTab() {
        return gameTab;
    }


}
