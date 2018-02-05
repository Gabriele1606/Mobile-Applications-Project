package com.example.gabri.firstapp;

import android.os.AsyncTask;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Gabri on 02/02/18.
 */

public class MyTask extends AsyncTask<String,Void,String> {
   EasyFlipView flipView;
    public MyTask(EasyFlipView flipView){
        super();
        this.flipView=flipView;
        this.flipView.setFlipDuration(1200);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Thread.sleep(1900);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        return "Executed";
    }

    @Override
    protected void onPreExecute()
    {
        flipView.flipTheView(true);


    }

    @Override
    protected void onPostExecute(String result){
        if(flipView.isFrontSide()==false)
        flipView.flipTheView(true);
    }


}
