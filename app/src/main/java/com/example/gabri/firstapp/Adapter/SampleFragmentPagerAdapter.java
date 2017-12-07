package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.gabri.firstapp.Controller.Filter;
import com.example.gabri.firstapp.FragmentPage1;
import com.example.gabri.firstapp.FragmentPageGames;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Platform;

import java.util.ArrayList;
import java.util.List;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private int PAGE_COUNT = 1;
    private String tabTitles[] = new String[6];
    private Context context;

    private List<Object> objectList;

    private ArrayList<Fragment> listFragment;
    private List<Platform> listPlatform;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, List<Object> objectList) {
        super(fm);
        this.context = context;
        listFragment= new ArrayList<>();
        this.objectList=objectList;
        tabTitles[0]="News";

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment current;
        System.out.println("--------------------------LA POSIZIONE E'"+ position);
        return createFragment(position);

    }

    public Fragment createFragment(int position){
        Fragment current;
        FragmentPage1 f1;
        FragmentPageGames f2;
        if(position==0){
            f1= new FragmentPage1();
            f1.setList(objectList);
            current=f1;
            listFragment.add(current);
            return current;
        }
        else{
            f2= new FragmentPageGames();
            f2.setDevelopName(getPageTitle(position).toString());
            current=f2;
            listFragment.add(current);
            return current;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public void notifyDataSetChanged() {
        Filter filter = new Filter();
        List<String> developerList;
        this.listPlatform = Data.getData().getListPlatform();
        developerList=filter.getDistinctDeveloperOrderedByNew(this.listPlatform);
        this.PAGE_COUNT+=5;
        for(int i=0;i<5;i++){
            this.tabTitles[i+1]=developerList.get(i);
        }
        super.notifyDataSetChanged();//questo deve essere lasciato per ultimo in quanto permette il refresh della pagina
    }
}