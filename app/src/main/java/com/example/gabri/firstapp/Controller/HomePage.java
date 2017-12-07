package com.example.gabri.firstapp.Controller;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.astuetz.PagerSlidingTabStrip;
import com.example.gabri.firstapp.API.PossibleAPI;
import com.example.gabri.firstapp.Adapter.CoverFlowAdapter;
import com.example.gabri.firstapp.Adapter.GameAdapter;
import com.example.gabri.firstapp.Adapter.SampleFragmentPagerAdapter;
import com.example.gabri.firstapp.GameEntity;
import com.example.gabri.firstapp.GameXML;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.GameCover;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.Model.Title;
import com.example.gabri.firstapp.PlatformXML;
import com.example.gabri.firstapp.R;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;


//import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class HomePage extends AppCompatActivity {

    //private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mAdapter;
    private ArrayList<GameEntity> mData = new ArrayList<>(0);
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<GameCover> gameCoverList;
    private RecyclerView recyclerViewTwo;
    private RecyclerView.Adapter adapterTwo;
    private List<GameCover> gameCoverListTwo;
    List<Object> listObject;
    APIManager apiManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        apiManager=new APIManager();

        MaterialViewPager mViewPager = (MaterialViewPager) findViewById(R.id.viewpager);
        ViewPager viewPager= mViewPager.getViewPager();
        //ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        listObject= Data.getInstance();

        SampleFragmentPagerAdapter sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), this, listObject);
        viewPager.setAdapter(sampleFragmentPagerAdapter);


        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        apiManager.setObserver(sampleFragmentPagerAdapter);
        apiManager.getPlatformFactory();
        // Give the TabLayout the ViewPager


        //TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //tabLayout.setupWithViewPager(viewPager);
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.materialviewpager_pagerTitleStrip);





/*
        mData.add(new GameEntity(R.drawable.image_1, R.string.title1));
        mData.add(new GameEntity(R.drawable.image_2, R.string.title2));
        mData.add(new GameEntity(R.drawable.image_3, R.string.title3));
        mData.add(new GameEntity(R.drawable.image_4, R.string.title4));
        mData.add(new GameEntity(R.drawable.image_5, R.string.title4));
        mData.add(new GameEntity(R.drawable.image_6, R.string.title4));



        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);


        mAdapter = new CoverFlowAdapter(this);
        mAdapter.setData(mData);
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
        mCoverFlow.setAdapter(mAdapter);*/








    }

/*
    public static int getDrawable(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coverflow_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


*/

}
