package com.example.gabri.firstapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.gabri.firstapp.Adapter.ReadLaterAdapter;
import com.example.gabri.firstapp.Adapter.WishListAdapter;
import com.example.gabri.firstapp.Controller.TimerLoad;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

/**
 * Created by Gabri on 24/12/17.
 */

public class FragmentReadLater extends android.support.v4.app.Fragment {

    private View view;
    private List<RSSFeed> newsList;
    private DBQuery dbQuery;
    private ListView listOfNewsToRead;
    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_read_later, container, false);

        this.listOfNewsToRead=(ListView)view.findViewById(R.id.news_read_later);

        this.mContext=container.getContext();
        newsList= new ArrayList<RSSFeed>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("news").child(Data.getIdUserForRemoteDb()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        newsList.clear();
                        for(DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                            //newsList.add((RSSFeed) idSnapshot.getValue());
                            HashMap<String, RSSFeed> newsHasMap= (HashMap<String, RSSFeed>) idSnapshot.getValue();
                            //newsList.add(new RSSFeed(newsHasMap.get("title"),newsHasMap.get("description"),newsHasMap.get("pubdate"),newsHasMap.get("guis"),newsHasMap.get("imageLink")));

                            System.out.println("DOVE SONoooooo----"+newsList.get(1));
                        }

                        ReadLaterAdapter adapter=new ReadLaterAdapter(mContext,R.layout.read_later_list_row,newsList);
                        listOfNewsToRead.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );












        return view;
    }

    public void prova(){

    }

}

