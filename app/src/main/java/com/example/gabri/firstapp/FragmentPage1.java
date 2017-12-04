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
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.RowGame;
import com.example.gabri.firstapp.Model.Title;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by simon on 18/11/2017.
 */

public class FragmentPage1 extends Fragment {

    private RecyclerView vrecyclerView;
    private List<Game> albumList;
    private List<List<Game>> listAlbumlist;
    private RecyclerAdapter recyclerAdapter;
    private List<Object> listObject= Data.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page1, container, false);

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

            if(!Data.getData().isInizialized()){
                initializeData();
                Data.getData().setInizialized();
            }

        return view;
    }

    private void initializeData(){

        albumList = new ArrayList<>();
        listAlbumlist= new ArrayList<List<Game>>();

        RowGame rowGame;




        prepareAlbums();
        for (int i=0; i<10;i++) {
            Data.getInstance().add(new RowGame(albumList));
            //listAlbumlist.add(albumList);
        }
        RowGame slider= new RowGame();
        slider.setSlider(true);

        recyclerAdapter.notifyDataSetChanged();

        APIManager apiManager= new APIManager();
        apiManager.getRssList(listObject,recyclerAdapter);
        apiManager.getPlatformFactory();
        //apiManager.getPlatformList();
    }


    public void notifyDataChange(){
        recyclerAdapter.notifyDataSetChanged();
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

    public void setList(List<Object> list) {
        this.listObject = list;
    }
}
