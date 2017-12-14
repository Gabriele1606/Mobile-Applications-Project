package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SampleFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {
    private int PAGE_COUNT = 1;
    private String tabTitles[] = new String[6];
    private Context context;

    private List<Object> objectList;

    private FragmentPage1 fragmentHome=null;
    private List<Platform> listPlatform;
    private List<Integer> numPages;
    private Map<Integer,FragmentPageGames> loadedFragments;
    List<String> developerList;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, List<Object> objectList) {
        super(fm);
        this.context = context;
        Data.getData().setSampleFragmentPagerAdapter(this);
        loadedFragments= new HashMap<Integer, FragmentPageGames>();
        numPages= new ArrayList<Integer>();
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
            if (fragmentHome==null){
            f1= new FragmentPage1();
            f1.setList(objectList);
            current=f1;
            return current;
            }
            return fragmentHome;
        }
        else{
            if (loadedFragments.get(position)==null) {
                Bundle bundle= new Bundle();
                numPages.add(new Integer(position));
                f2 = new FragmentPageGames();
                //f2.setObserver(this);
                bundle.putString("name",getPageTitle(position).toString());
                f2.setArguments(bundle);
                //f2.setDevelopName();
                current = f2;
                loadedFragments.put(position,f2);
                return current;
            }
            return loadedFragments.get(position);
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
        for (Integer i :
                numPages) {
            if (loadedFragments.get(i)!=null){
                FragmentPageGames f1 = (FragmentPageGames) loadedFragments.get(i);
                System.out.println("FRAGMENT PAGER NOTIFICA:"+i);
                f1.notifyDataChange();
            }
        }
        super.notifyDataSetChanged();//questo deve essere lasciato per ultimo in quanto permette il refresh della pagina
    }

    private void loadTab(List<Platform> listPlatform) {
        if (listPlatform != null) {
            Filter filter = new Filter();
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

    public void notifyDataSetChanged(String developName) {
        for (Integer i :
                numPages) {
            if (loadedFragments.get(i)!=null&&(i-1)<developerList.size()){
                if (developName.equals(developerList.get(i-1))){
                FragmentPageGames f1 = (FragmentPageGames) loadedFragments.get(i);
                System.out.println("FRAGMENT PAGER NOTIFICA:"+i);
                f1.notifyDataChange();
                super.notifyDataSetChanged();
                }
            }
        }
    }
}