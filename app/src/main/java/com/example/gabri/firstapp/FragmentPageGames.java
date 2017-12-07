package com.example.gabri.firstapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabri.firstapp.Adapter.RecyclerAdapter;
import com.example.gabri.firstapp.Controller.APIManager;
import com.example.gabri.firstapp.Controller.Filter;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.ImgSlider;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.Model.RowGame;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by gabry on 18/11/2017.
 */

public class FragmentPageGames extends Fragment {

    private RecyclerView vrecyclerView;
    private List<Game> albumList;
    private List<List<Game>> listAlbumlist;
    private RecyclerAdapter recyclerAdapter;
    private String developName;
    private List<Object> listObject= new ArrayList<Object>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page_game, container, false);

        vrecyclerView= (RecyclerView) view.findViewById(R.id.v_recyclerView);

        recyclerAdapter= new RecyclerAdapter(view.getContext(),listObject);
        RecyclerView.LayoutManager vLayoutManager= new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false);
        vrecyclerView.setLayoutManager(vLayoutManager);
        vrecyclerView.setItemAnimator(new DefaultItemAnimator());
        //Animation Adapter
        ScaleInAnimationAdapter animatorAdapter= new ScaleInAnimationAdapter(recyclerAdapter);
        animatorAdapter.setFirstOnly(false);
        animatorAdapter.setDuration(300);
        vrecyclerView.setAdapter(animatorAdapter);
        if(!Data.getData().isInitialized(this.developName)){
            initializeData();
            Data.getData().setInitialized(this.developName);
        }


        return view;
    }

    private void initializeData(){
        Filter filter=new Filter();
        ImgSlider imgSlider= new ImgSlider();
        List<Platform> platformListOfSpecificDeveloper;
        platformListOfSpecificDeveloper=filter.getPlatformFromDeveloper(this.developName);


        List<String> urlImages=filter.getImageOfNewestGame(platformListOfSpecificDeveloper);


        urlImages=new ArrayList<String>();
        urlImages.add("http://thegamesdb.net/banners/fanart/original/34280-1.jpg");
        imgSlider.setUrlImages(urlImages);
        listObject.add(imgSlider);
        recyclerAdapter.notifyDataSetChanged();


    }


    public void notifyDataChange(){
        recyclerAdapter.notifyDataSetChanged();
    }



    public void setList(List<Object> list) {
        this.listObject = list;
    }

    public void setDevelopName(String developName){
        this.developName=developName;

    }
}
