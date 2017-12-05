package com.example.gabri.firstapp.Controller;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;

import com.example.gabri.firstapp.FragmentPage1;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by simon on 04/12/2017.
 */

public class TimerSlider extends TimerTask {
    ViewPager viewPager;
    List<String> urlImages;
    Context context;
    Activity activity;
    public TimerSlider(Context context, ViewPager viewPager, List<String> urlImages){
        this.viewPager=viewPager;
        this.urlImages=urlImages;
        this.context=context;
        activity=(Activity)context;
    }


    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int i=viewPager.getCurrentItem();
                if(urlImages.size()>0) {
                    if (i < (urlImages.size() -1)) {
                        viewPager.setCurrentItem(i+1);
                    } else if(i == (urlImages.size() -1)){
                        viewPager.setCurrentItem(0);
                    }
                }
            }
        });

    }
}
