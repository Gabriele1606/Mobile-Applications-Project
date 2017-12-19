package com.example.gabri.firstapp.Controller;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;

import com.example.gabri.firstapp.FragmentPageGames;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by simon on 04/12/2017.
 */

public class TimerLoad extends TimerTask {
    FragmentPageGames fragmentPageGames;
    Activity activity;
    public TimerLoad( Context context,FragmentPageGames fragmentPageGames){
        this.fragmentPageGames=fragmentPageGames;
        activity=(Activity)context;
    }


    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragmentPageGames.notifyDataChange();
            }
        });
        this.cancel();
    }
}
