package com.example.gabri.firstapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabri.firstapp.Adapter.RecyclerAdapter;
import com.example.gabri.firstapp.Controller.APIManager;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.ImgSlider;
import com.example.gabri.firstapp.Model.RowGame;
import com.example.gabri.firstapp.Model.Title;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by simon on 18/11/2017.
 */

public class FragmentPage1 extends Fragment{

    private RecyclerView vrecyclerView;
    private List<Game> albumList;
    private List<List<Game>> listAlbumlist;
    private RecyclerAdapter recyclerAdapter;
    private List<Object> listObject= Data.getInstance();
    private View view;
    SwipeRefreshLayout swipeContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_page1, container, false);




        //SWIPETOREFRESH
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        vrecyclerView= (RecyclerView) view.findViewById(R.id.v_recyclerView);

        recyclerAdapter= new RecyclerAdapter(view.getContext(),listObject);

        RecyclerView.LayoutManager vLayoutManager= new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false);
        vrecyclerView.setLayoutManager(vLayoutManager);
        vrecyclerView.setItemAnimator(new SlideInDownAnimator());
        vrecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL));
        //Animation Adapter
        ScaleInAnimationAdapter animatorAdapter= new ScaleInAnimationAdapter(recyclerAdapter);
        animatorAdapter.setFirstOnly(false);
        animatorAdapter.setDuration(300);
        vrecyclerView.setAdapter(animatorAdapter);

        //LISTENER REFRESH
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerAdapter.clear();
                initializeData();
                swipeContainer.setRefreshing(false);
            }
        });

            if(!Data.getData().isInitialized("NEWS")){
                initializeData();
                Data.getData().setInitialized("NEWS");
            }

        return view;
    }

    private void initializeData(){


        Data.getInstance().add(new UserInfo());
        recyclerAdapter.notifyDataSetChanged();


        albumList = new ArrayList<>();
        listAlbumlist= new ArrayList<List<Game>>();

        RowGame slider= new RowGame();
        slider.setSlider(true);



        recyclerAdapter.notifyDataSetChanged();

        APIManager apiManager= new APIManager();

        listObject.add(new Title("News"));

        apiManager.getRssList(listObject,recyclerAdapter);

    }



    public void setList(List<Object> list) {
        this.listObject = list;
    }
}
