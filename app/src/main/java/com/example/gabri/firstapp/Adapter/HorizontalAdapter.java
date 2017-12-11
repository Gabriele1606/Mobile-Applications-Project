package com.example.gabri.firstapp.Adapter;

/**
 * Created by simon on 09/11/2017.
 */

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gabri.firstapp.DBQuery;
import com.example.gabri.firstapp.FragmentGameDetail;
import com.example.gabri.firstapp.FragmentPage1;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.R;

import java.util.List;



/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    private Context mContext;
    private List<Game> gameList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        private CardView cardView;
        public MyViewHolder(View view) {
            super(view);
            int index;
            title =(TextView) view.findViewById(R.id.titleGame);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.cardView=(CardView) view.findViewById(R.id.cardView);


        }
    }


    public HorizontalAdapter(Context mContext, List<Game> gameList) {
        this.mContext = mContext;
        this.gameList = gameList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        int index;
        DBQuery dbQuery=new DBQuery();
        final Game game = gameList.get(position);
        index=game.getGameTitle().indexOf(":");
       // when click on game
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                FragmentGameDetail temp=new FragmentGameDetail();
                Bundle bundle=new Bundle();
                bundle.putString("testo","ciaooooooo");
                Activity activity = (Activity) mContext;
                activity.getFragmentManager().beginTransaction().replace(R.id.homepage,temp).addToBackStack(null).commit();
                System.out.println("click");
            }
        });

        if(index!=-1)
            holder.title.setText(game.getGameTitle().substring(0, index));

        else
            holder.title.setText(game.getGameTitle());

        // loading game cover using Glide library
        Glide.with(mContext).load("http://thegamesdb.net/banners/"+dbQuery.getBoxArtFromGame(game).getThumb()).into(holder.thumbnail);


    }

    /**
     * Showing popup menu when tapping on 3 dots
     */


    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}