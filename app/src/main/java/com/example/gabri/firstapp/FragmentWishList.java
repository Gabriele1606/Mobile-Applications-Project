
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

import com.example.gabri.firstapp.Adapter.WishListAdapter;
import com.example.gabri.firstapp.Controller.HomePage;
import com.example.gabri.firstapp.Controller.TimerLoad;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by Gabri on 24/12/17.
 */

public class FragmentWishList extends android.support.v4.app.Fragment {

    private View view;
    private List<Integer> idGame;
    private DBQuery dbQuery;
    private ListView listOfWishGame;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_wish_list, container, false);

        this.listOfWishGame=(ListView)view.findViewById(R.id.list_view);
        idGame= new ArrayList<Integer>();
        dbQuery=new DBQuery();
        this.mContext=container.getContext();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference.child("game").child(Data.getIdUserForRemoteDb()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        idGame.clear();
                        for(DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                            idGame.add((int)(long) idSnapshot.getValue());
                        }
                        List<Game> gameList=new ArrayList<Game>();
                        for(int i=0;i<idGame.size();i++) {
                            gameList.add(dbQuery.getGameFromId(idGame.get(i)));
                        }
                        WishListAdapter adapter=new WishListAdapter(mContext,R.layout.wish_list_row,gameList);
                        listOfWishGame.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


        return view;
    }

}
