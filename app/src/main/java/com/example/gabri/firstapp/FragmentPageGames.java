package com.example.gabri.firstapp;

import android.net.wifi.p2p.WifiP2pManager;
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
import com.example.gabri.firstapp.Model.Game_Table;
import com.example.gabri.firstapp.Model.ImgSlider;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.Model.Platform_Table;
import com.example.gabri.firstapp.Model.RowGame;
import com.raizlabs.android.dbflow.sql.language.SQLite;

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
            System.out.println("INIZIALIZZO FRAGMENT");
            initializeData();
            Data.getData().setInitialized(this.developName);
        }




        return view;
    }

    private void initializeData(){
        listObject.clear();
        Filter filter=new Filter();
        ImgSlider imgSlider= new ImgSlider();
        //carico alcuni dati
        APIManager apiManager=new APIManager();

        platformOfSpecifiedDeveloper=filter.getPlatformFromDeveloper(this.developName);
        listObject.add(new ImgSlider());
        //apiManager.getGameDetail(platformOfSpecifiedDeveloper,recyclerAdapter,listObject);
        ArrayList<Integer> ids = new ArrayList<>();
        DBQuery dbQuery=new DBQuery();

        List<PlatformDetail> platformDetails=dbQuery.getPlatformDetailFromDeveloper(developName);
        List<Platform> platforms =  dbQuery.getPlatformFromPlarformDetail(platformDetails);
        List<Game> games = dbQuery.getGameFromAllPlatfoms(platforms);
        List<Fanart> fanarts = dbQuery.getFanartFromGame(games);

        List<String> urlImages= new ArrayList<String>();
        for (Fanart f :
                fanarts) {
            if (urlImages.size()>4)//A COSA SERVE?
                break;
            urlImages.add(new String("http://thegamesdb.net/banners/"+f.getThumb()));
        }
        
        //List<String> urlImages=filter.getLinkImagesForSlider(platformListOfSpecificDeveloper);

        imgSlider.setUrlImages(urlImages);
        listObject.add(imgSlider);


        albumList = new ArrayList<>();
        listAlbumlist= new ArrayList<List<Game>>();


       for (int i=0; i<platforms.size();i++) {
            List<Game> gameList =dbQuery.getGameFromPlatfom(platforms.get(i));
            Data.getInstance().add(new RowGame(gameList));

        }
        RowGame slider= new RowGame();
        slider.setSlider(true);




        recyclerAdapter.notifyDataSetChanged();
    }


    public void notifyDataChange(){
        initializeData();
        recyclerAdapter.notifyDataSetChanged();
    }



    public void setList(List<Object> list) {
        this.listObject = list;
    }

    public void setDevelopName(String developName){
        this.developName=developName;

    }



    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        Game a = new Game("True Romance", 13, covers[0]);
        albumList.add(a);

        a = new Game("Xscpae", 8, covers[1]);
        albumList.add(a);

        a = new Game("Maroon 5", 11, covers[2]);
        albumList.add(a);

        a = new Game("Born to Die", 12, covers[3]);
        albumList.add(a);

        a = new Game("Honeymoon", 14, covers[4]);
        albumList.add(a);

        a = new Game("I Need a Doctor", 1, covers[5]);
        albumList.add(a);

        a = new Game("Loud", 11, covers[6]);
        albumList.add(a);

        a = new Game("Legend", 14, covers[7]);
        albumList.add(a);

        a = new Game("Hello", 11, covers[8]);
        albumList.add(a);

        a = new Game("Greatest Hits", 17, covers[9]);
        albumList.add(a);

        //adapter.notifyDataSetChanged();
    }

}
