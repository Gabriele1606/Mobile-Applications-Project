package com.example.gabri.firstapp.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gabri.firstapp.Adapter.WishListAdapter;
import com.example.gabri.firstapp.DBQuery;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Gabri on 04/02/18.
 */

public class GameTabFragment extends Fragment {
    private RecyclerView recyclerView;
    private WishListAdapter mAdapter;
    private View view;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.game_tab_fragment, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_gamesearch);
        this.mLayoutManager = new LinearLayoutManager(getApplicationContext());

        return view;
    }

    public void fillRecycleView(List<Game> gameList){
        mAdapter = new WishListAdapter(gameList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
