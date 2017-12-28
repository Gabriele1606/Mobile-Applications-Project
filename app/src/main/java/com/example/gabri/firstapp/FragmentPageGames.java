package com.example.gabri.firstapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.gabri.firstapp.Adapter.RecyclerAdapter;
import com.example.gabri.firstapp.Adapter.SampleFragmentPagerAdapter;
import com.example.gabri.firstapp.Controller.APIManager;
import com.example.gabri.firstapp.Controller.Filter;
import com.example.gabri.firstapp.Controller.TimerLoad;
import com.example.gabri.firstapp.Controller.TimerSlider;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.ImgSlider;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.Model.RowGame;
import com.example.gabri.firstapp.Model.Title;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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
    private Title title= new Title("Wait, I'm Loading");
    private  List<Fanart> fanarts;
    List<GameDetail> gameDetail;
    SwipeRefreshLayout swipeContainer;
    APIManager apiManager = new APIManager();
    private boolean timerStarted=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_page_game, container, false);
        Bundle arguments = this.getArguments();
        developName = arguments.getString("name");
        observer=Data.getData().getSetSampleFragmentPagerAdapter();

        startRecyclerView(listObject);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerGame);

        //carico alcuni dati
        /*APIManager apiManager=new APIManager();
        apiManager.setObserver(this.observer);
        apiManager.getGameDetail(platformOfSpecifiedDeveloper,recyclerAdapter,listObject);*/
        final FragmentPageGames fragment= this;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiManager.setObserver(observer);
                apiManager.getAllGameDetails(platformOfSpecifiedDeveloper, developName);
                if (listObject.remove(title))
                    recyclerAdapter.notifyItemRemoved(1);
                listObject.add(1,title);
                recyclerAdapter.notifyItemInserted(1);
                swipeContainer.setRefreshing(false);
                if(!timerStarted) {
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerLoad(Data.getData().getHomePageActivity(), fragment), 10000, 3000);
                    timerStarted =true;
                }
            }
        });



            System.out.println("INIZIALIZZO FRAGMENT");
            initializeData();




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
        gameDetail = dbQuery.getGameDetail(games);
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
            Title title=new Title(platforms.get(i).getName());
            listObject.add(title);
           RowGame rowGame = new RowGame(gameList);
           if (platforms.size()==1)
               rowGame.setGrid(true);
           listObject.add(rowGame);

        }
        RowGame slider= new RowGame();
        slider.setSlider(true);


        if(listObject.size()==0){
            ImageView logobk = (ImageView) view.findViewById(R.id.logoBk);
            logobk.setVisibility(View.VISIBLE);
        }
        System.out.println("MODIFICO I DATI");
        recyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (gameDetail!=null) {
                if (gameDetail.size() == 0) {
                    swipeContainer.setRefreshing(true);
                    apiManager.setObserver(observer);
                    apiManager.getAllGameDetails(platformOfSpecifiedDeveloper, developName);
                    swipeContainer.setRefreshing(false);
                }
            }
        }
    }

    public void notifyDataChange(){
        initializeData();
        startRecyclerView(this.listObject);
        System.out.println("NOTIFICO RECYCLER");
        recyclerAdapter.notifyDataSetChanged();
        timerStarted=false;
    }



    public void setList(List<Object> list) {
        this.listObject = list;
    }

    /*public void setDevelopName(String developName){
        this.developName=developName;

    }*/
    public void setObserver(SampleFragmentPagerAdapter observer){
        this.observer=observer;
    }

}
