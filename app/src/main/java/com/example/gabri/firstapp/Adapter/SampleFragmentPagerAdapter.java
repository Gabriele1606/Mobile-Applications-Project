package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.gabri.firstapp.Controller.Filter;
import com.example.gabri.firstapp.FragmentPage1;
import com.example.gabri.firstapp.FragmentPageGames;
import com.example.gabri.firstapp.Model.AppDatabase;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Platform;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Calendar;
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
        Filter filter= new Filter();
        //LOAD DATA ALREADY IN DB
        System.out.println("QUESTO E' IL DATABASE: ----->"+FlowManager.getDatabase(AppDatabase.class).getDatabaseName());
        List<Platform> platforms = SQLite.select().from(Platform.class).queryList();
        filter.orderPlatformFromNewestToHolder(platforms);
        loadTab(platforms);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment current;
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
        this.listPlatform = Data.getData().getListPlatform();
        loadTab(listPlatform);
        super.notifyDataSetChanged();//questo deve essere lasciato per ultimo in quanto permette il refresh della pagina
    }

    private void loadTab(List<Platform> listPlatform) {
        if (listPlatform != null) {
            Filter filter = new Filter();
            List<String> developerList;
            developerList = filter.getDistinctDeveloperOrderedByNew(listPlatform);
            if (developerList != null) {
                if (!developerList.isEmpty()) {
                    for (int i = 0; i < 5&&i<developerList.size(); i++) {
                        this.tabTitles[i + 1] = developerList.get(i);
                        this.PAGE_COUNT=2+i;
                    }
                    System.out.println("AGGIUNTI I TAB: " + Calendar.getInstance().getTime());
                }
            }
        }
    }
}