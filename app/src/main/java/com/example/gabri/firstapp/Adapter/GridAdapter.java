package com.example.gabri.firstapp.Adapter;

/**
 * Created by simon on 09/11/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gabri.firstapp.DBQuery;
import com.example.gabri.firstapp.FragmentGameDetail;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.R;

import java.util.List;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {

    private Context mContext;
    private List<Game> gameList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView title, count;
        public ImageView thumbnail;
        public ProgressBar progressBar;
        public MyViewHolder(View view) {
            super(view);
            this.view= view;
            int index;
            title =(TextView) view.findViewById(R.id.titleGame);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            progressBar=(ProgressBar) view.findViewById(R.id.progressCard);

        }
    }


    public GridAdapter(Context mContext, List<Game> gameList) {
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
        GridAdapter old= this;

        holder.view.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                FragmentGameDetail fragmentGameDetail=new FragmentGameDetail();
                Bundle bundle=new Bundle();
                bundle.putInt("GAME ID",game.getId());
                fragmentGameDetail.setArguments(bundle);
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);

                //FINAL SOLUTION
                Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentGameDetail, "GameDetail");
                transaction.addToBackStack("TABLAYOUT");
                transaction.commit();

            }
        });

        if(index!=-1)
            holder.title.setText(game.getGameTitle().substring(0, index));

        else
            holder.title.setText(game.getGameTitle());

        // loading game cover using Glide library
        Glide.with(mContext).load("http://thegamesdb.net/banners/"+dbQuery.getBoxArtFromGame(game).getThumb()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.thumbnail);

    }
    @Override
    public int getItemCount() {
        return gameList.size();
    }
}