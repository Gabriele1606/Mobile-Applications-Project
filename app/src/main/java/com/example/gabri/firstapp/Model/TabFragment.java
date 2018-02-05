package com.example.gabri.firstapp.Model;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabri.firstapp.Adapter.SampleFragmentPagerAdapter;
import com.example.gabri.firstapp.Controller.APIManager;
import com.example.gabri.firstapp.Controller.HomePage;
import com.example.gabri.firstapp.R;
import com.google.zxing.aztec.encoder.HighLevelEncoder;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Calendar;

/**
 * Created by simon on 12/12/2017.
 */

public class TabFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        APIManager apiManager = new APIManager();
        View view = inflater.inflate(R.layout.tab_fragment, container, false);
        final ViewPager viewPager=(ViewPager) view.findViewById(R.id.viewpager);
        SampleFragmentPagerAdapter sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getChildFragmentManager(), view.getContext(), Data.getInstance());
        viewPager.setAdapter(sampleFragmentPagerAdapter);
        apiManager.setObserver(sampleFragmentPagerAdapter);
        Data.getData().setHomePageViewPager(viewPager);
        System.out.println("START RICHIESTE API: "+ Calendar.getInstance().getTime());
        apiManager.setNumPlatformInDatabase(SQLite.select().from(Platform.class).queryList().size());
        apiManager.getPlatformFactory();
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }

    @Override
    public void onResume() {
        if (getActivity()instanceof HomePage) {
            HomePage activity = (HomePage) getActivity();
            activity.HighlightSection("Home");
        }
        super.onResume();
    }
}
