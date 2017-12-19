package com.example.gabri.firstapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Gabri on 14/12/17.
 */

public class FragmentNewsDetail extends Fragment {

    private View viewRoot;
    private Context mContext;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewRoot=inflater.inflate(R.layout.fragment_news_detail,container,false);
        this.inflater=inflater;


        return this.viewRoot;

    }
}
