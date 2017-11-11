package com.example.gabri.firstapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import junit.framework.Assert;

import java.util.ArrayList;


import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;


public class HomePage extends ActionBarActivity {

    private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mAdapter;
    private ArrayList<GameEntity> mData = new ArrayList<>(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

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
        mCoverFlow.setAdapter(mAdapter);



        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        for (int i = 0; i < 6; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);

            imageView.setPadding(2, 2, 200, 2);
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), getDrawable(this,"image_"+(i+1))));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);


            layout.addView(imageView);

            imageView.getLayoutParams().height=500;
            imageView.getLayoutParams().width=600;
            imageView.requestLayout();
        }

        LinearLayout layout2 = (LinearLayout) findViewById(R.id.linear2);
        for (int i = 0; i < 6; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(2, 2, 200, 2);
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), getDrawable(this,"image_"+(i+1))));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            layout2.addView(imageView);
            imageView.getLayoutParams().height=500;
            imageView.getLayoutParams().width=600;
            imageView.requestLayout();
        }



    }

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
}
