package com.example.gabri.firstapp.Adapter;

/**
 * Created by simon on 09/11/2017.
 */

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.gabri.firstapp.Controller.APIManager;
import com.example.gabri.firstapp.DBQuery;
import com.example.gabri.firstapp.FragmentGameDetail;
import com.example.gabri.firstapp.FragmentPage1;
import com.example.gabri.firstapp.GameDetail;
import com.example.gabri.firstapp.GameDetail_Table;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.Game_Table;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.R;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;




public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    private final RecyclerView recyclerView;
    private Context mContext;
    private List<Game> gameList;
    private int visibleThreshold = 20;
    private int lastVisibleItem, totalItemCount,visibleItemCount;
    boolean isLoading;
    public HorizontalAdapter(Context mContext, List<Game> list, RecyclerView recelement) {
        this.mContext = mContext;
        this.gameList = list;
        this.recyclerView=recelement;
        final HorizontalAdapter horizontalAdapter=this;
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (dx!=0||dy!=0){
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleItemCount)) {
                    //API
                    APIManager apiManager = new APIManager();
                    if (gameList != null)
                        if (gameList.size() > 0) {
                            System.out.println("CHIEDO NUOVI GIOCHI");
                            Game game = gameList.get(0);
                            String platformName = game.getPlatform();
                            DBQuery dbQuery = new DBQuery();
                            Platform platform = dbQuery.getPlatform(platformName);
                            apiManager.loadMoreGame(platform, horizontalAdapter);
                            isLoading = true;
                        }
                }
            }
            }

        });
    }



    public void addNewIds(List<Integer> idsNewGameDetail) {
        if(gameList!=null){
            List<Game> newGames=SQLite.select().from(Game.class).where(Game_Table.id.in(idsNewGameDetail)).queryList();
            int size = gameList.size();
            int newsize=size;
            for (Game g :
                    newGames) {
                if (!gameList.contains(g)){
                    gameList.add(g);
                    newsize++;
                }
            }
            notifyItemRangeInserted((size+1),newsize);
            setLoaded();
        }
    }



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
        HorizontalAdapter old= this;

        holder.view.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                FragmentGameDetail fragmentGameDetail=new FragmentGameDetail();
                Bundle bundle=new Bundle();
                bundle.putInt("GAME ID",game.getId());
                fragmentGameDetail.setArguments(bundle);
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
                if(!TWOPANELS){
                //FINAL SOLUTION
                Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentGameDetail, "GameDetail");
                transaction.addToBackStack("TABLAYOUT");
                transaction.commit();
                }else{
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framegameDetail, fragmentGameDetail, "GameDetail");
                    transaction.commit();
                    Data.getData().getHomePageActivity().enlargeDetailGame();
                }


                /*
                Intent intent =  new Intent(mContext, FragmentGameDetail.class);
                intent.putExtra("GAME ID", game.getId());
                mContext.startActivity(intent);*/


                /*
                FragmentGameDetail temp=new FragmentGameDetail();
                Bundle bundle=new Bundle();
                bundle.putString("testo","ciaooooooo");
                Activity activity = (Activity) mContext;
                activity.getFragmentManager().beginTransaction().replace(R.id.homepage,temp).addToBackStack(null).commit();
                System.out.println("click");*/
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
        if (gameList!=null)
            return gameList.size();
        return 0;
    }
    public void setLoaded() {
        isLoading = false;
    }
}