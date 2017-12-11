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
import com.example.gabri.firstapp.Adapter.SampleFragmentPagerAdapter;
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
    private List<Platform> platformOfSpecifiedDeveloper=new ArrayList<Platform>();
    private SampleFragmentPagerAdapter observer;
    private View view;
    APIManager apiManager = new APIManager();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_page_game, container, false);

        startRecyclerView(listObject);


        //carico alcuni dati
        /*APIManager apiManager=new APIManager();
        apiManager.setObserver(this.observer);
        apiManager.getGameDetail(platformOfSpecifiedDeveloper,recyclerAdapter,listObject);*/



        if(!Data.getData().isInitialized(this.developName)){
            System.out.println("INIZIALIZZO FRAGMENT");
            initializeData();
            Data.getData().setInitialized(this.developName);
            apiManager.setObserver(observer);
            apiManager.getAllGameDetails(platformOfSpecifiedDeveloper, this.developName);
        }




        return view;
    }

    private void startRecyclerView(List<Object> listObject) {
        vrecyclerView= (RecyclerView) view.findViewById(R.id.recyclerGame);

        recyclerAdapter= new RecyclerAdapter(view.getContext(),listObject);
        RecyclerView.LayoutManager vLayoutManager= new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false);
        vrecyclerView.setLayoutManager(vLayoutManager);
        vrecyclerView.setItemAnimator(new DefaultItemAnimator());
        //Animation Adapter
        ScaleInAnimationAdapter animatorAdapter= new ScaleInAnimationAdapter(recyclerAdapter);
        animatorAdapter.setFirstOnly(false);
        animatorAdapter.setDuration(300);
        vrecyclerView.setAdapter(animatorAdapter);
    }

    private void initializeData(){
        listObject.clear();
        Filter filter=new Filter();
        ImgSlider imgSlider= new ImgSlider();

        platformOfSpecifiedDeveloper=filter.getPlatformFromDeveloper(this.developName);

        DBQuery dbQuery=new DBQuery();

        List<PlatformDetail> platformDetails=dbQuery.getPlatformDetailFromDeveloper(developName);
        List<Platform> platforms =  dbQuery.getPlatformFromPlarformDetail(platformDetails);
        this.platformOfSpecifiedDeveloper=platforms;
        List<Game> games = dbQuery.getGameFromAllPlatfoms(platforms);
        List<Fanart> fanarts = dbQuery.getFanartFromGameList(games);

        List<String> urlImages= new ArrayList<String>();
        System.out.println("NUMERO FANARTS:----"+fanarts.size());
        for (Fanart f :
                fanarts) {
            if (urlImages.size()>4)//A COSA SERVE?
                break;
            urlImages.add(new String("http://thegamesdb.net/banners/"+f.getThumb()));
        }
        
        //List<String> urlImages=filter.getLinkImagesForSlider(platformListOfSpecificDeveloper);
        if (urlImages.size()>0) {
            imgSlider.setUrlImages(urlImages);
            System.out.println("LO SLIDER HA:" + imgSlider.getUrlImages());
            listObject.add(0,imgSlider);
        }

        albumList = new ArrayList<>();
        listAlbumlist= new ArrayList<List<Game>>();


       for (int i=0; i<platforms.size();i++) {
            List<Game> gameList =dbQuery.getGameFromPlatfom(platforms.get(i));
            listObject.add(new RowGame(gameList));

        }
        RowGame slider= new RowGame();
        slider.setSlider(true);




        recyclerAdapter.notifyDataSetChanged();


    }


    public void notifyDataChange(){
        initializeData();
        startRecyclerView(this.listObject);
        System.out.println("NOTIFICO RECYCLER");
        recyclerAdapter.notifyDataSetChanged();
    }



    public void setList(List<Object> list) {
        this.listObject = list;
    }

    public void setDevelopName(String developName){
        this.developName=developName;

    }
    public void setObserver(SampleFragmentPagerAdapter observer){
        this.observer=observer;
    }

}
