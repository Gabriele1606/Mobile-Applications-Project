package com.example.gabri.firstapp.Adapter;

/**
 * Created by simon on 09/11/2017.
 */

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
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
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

        visibleItemCount = layoutManager.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        lastVisibleItem = layoutManager.findLastVisibleItemPosition();

        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleItemCount)) {
            //API
            APIManager apiManager = new APIManager();
            if (gameList != null)
                if (gameList.size() > 0) {
                    System.out.println("CHIEDO NUOVI GIOCHI");
                    Game game = gameList.get(0);
                    String platformName = game.getPlatform();
                    System.out.println(platformName);
                    DBQuery dbQuery = new DBQuery();
                    Platform platform = dbQuery.getPlatform(platformName);
                    apiManager.loadMoreGame(platform, horizontalAdapter);
                    isLoading = true;
                }
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();


                if (dx!=0||dy!=0){
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleItemCount)) {
                    System.out.println("TRIGGERED LOAD MORE: ");
                    //API
                    APIManager apiManager = new APIManager();
                    if (gameList != null)
                        if (gameList.size() > 0) {
                            System.out.println("CHIEDO NUOVI GIOCHI");
                            Game game = gameList.get(0);
                            String platformName = game.getPlatform();
                            System.out.println(platformName);
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
        public TextView title,pubdate, count;
        public ImageView thumbnail;
        public ProgressBar progressBar;
        public MyViewHolder(View view) {
            super(view);
            this.view= view;
            int index;
            title =(TextView) view.findViewById(R.id.titleGame);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            progressBar=(ProgressBar) view.findViewById(R.id.progressCard);
            //progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#67b8d6")));
            pubdate=(TextView) view.findViewById(R.id.releaseDate);


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
        Glide.with(mContext).asBitmap().load(R.color.transparent).into(holder.thumbnail);

        // when click on game
        HorizontalAdapter old= this;
        holder.view.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                FragmentGameDetail fragmentGameDetail=new FragmentGameDetail();
                Bundle bundle=new Bundle();
                bundle.putSerializable("REALGAMEOBJECT",game);
                fragmentGameDetail.setArguments(bundle);
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);

                //FINAL SOLUTION
                Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentGameDetail, "GameDetail");
                transaction.addToBackStack("TABLAYOUT");
                transaction.commit();



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

        if(index!=-1) {
            holder.title.setText(game.getGameTitle().substring(0, index));

        }else {
            holder.title.setText(game.getGameTitle());
        }

        if(game.getReleaseDate().equals("null")){
            holder.pubdate.setText("31/12/2006");
        }else{
            holder.pubdate.setText(game.getReleaseDate());
        }

        //Check if thumb is in Firebase

       /* String string="http://thegamesdb.net/banners/boxart/thumb/original/front/9040-1.jpg";
        String match="([0-9].*\\.jpg?)";
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find())
        System.out.println("MATCH: "+matcher.group(1));*/
        Pattern pattern=null;
        Matcher matcher=null;

         String regex = "([0-9].*\\.jpg?)";
         String string = dbQuery.getBoxArtFromGame(game).getThumb();
        final String thumb = dbQuery.getBoxArtFromGame(game).getThumb();
        if (string!=null) {
              pattern = Pattern.compile(regex);
              matcher = pattern.matcher(string);
        }
        if (string!=null){
            if (matcher.find()) {
                System.out.println("Group " + ": " + matcher.group(1));
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference.child("thumbs/" + matcher.group(1)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //System.out.println("LOADED FROM FIREBASE :" + uri.toString());
                        Glide.with(mContext).asBitmap().load(uri).listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(holder.thumbnail);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("NOT LOADED FROM FIREBASE :" +"http://thegamesdb.net/banners/"+ thumb);
                        loadFromTheGamesDb(holder, game);
                    }
                });
            }
        }else
            loadFromTheGamesDb(holder,game);

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



    private void loadFromTheGamesDb(final MyViewHolder holder, Game game){
        DBQuery dbQuery= new DBQuery();
        Glide.with(mContext).load("http://thegamesdb.net/banners/"+dbQuery.getBoxArtFromGame(game).getThumb()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.thumbnail);
    }
}