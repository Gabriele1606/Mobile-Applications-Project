
package com.example.gabri.firstapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gabri.firstapp.Boxart;
import com.example.gabri.firstapp.Controller.HomePage;
import com.example.gabri.firstapp.DBQuery;
import com.example.gabri.firstapp.FragmentGameDetail;

import com.example.gabri.firstapp.FragmentNewsDetail;
import com.example.gabri.firstapp.FragmentWishList;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;

import com.example.gabri.firstapp.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by Gabri
 */

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private List<Game> wishListGame;
    private FragmentWishList fragmentWishList;

    public void setFragmentWishList(FragmentWishList fragmentWishList) {
        this.fragmentWishList = fragmentWishList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView coverImage;
        private ImageView garbage;
        private TextView gameTitle;
        private TextView pubDate;
        private TextView console;
        private Game game;
        private Context mContex;
        private View view;
        private ImageView shareButton;


        public ViewHolder(View view) {
            super(view);
            coverImage = (ImageView) view.findViewById(R.id.wish_game_cover);
            gameTitle = (TextView) view.findViewById(R.id.game_row_title);
            pubDate = (TextView) view.findViewById(R.id.pub_date_game_row);
            console=(TextView) view.findViewById(R.id.console_game_row);
            shareButton=(ImageView) view.findViewById(R.id.facebook_share);
            this.view=view;

        }

    }


    public WishListAdapter(List<Game> wishListGame) {
        this.wishListGame=wishListGame;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Game game = wishListGame.get(position);
        if (game != null) {
            holder.gameTitle.setText(game.getGameTitle());
            holder.console.setText(game.getPlatform());
            DBQuery dbQuery = new DBQuery();
            Boxart boxart = dbQuery.getBoxArtFromGame(game);


            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fragmentWishList!=null)
                    fragmentWishList.setFacebookShare(game);
                }
            });

            if (game.getReleaseDate().equals("null")) {
                holder.pubDate.setText("31/12/2006");
            } else {
                holder.pubDate.setText(game.getReleaseDate());
            }

            Glide.with(getContext()).load("http://thegamesdb.net/banners/" + boxart.getThumb()).into(holder.coverImage);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
                    Bundle bundle = new Bundle();
                    bundle.putString("IDFIREBASE", game.getIdForFirebase());
                    bundle.putSerializable("REALGAMEOBJECT", game);


                    FragmentGameDetail fragmentGameDetail = new FragmentGameDetail();
                    fragmentGameDetail.setArguments(bundle);
                    //FINAL SOLUTION
                    Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentGameDetail, "GameDetail");
                    transaction.addToBackStack("TABLAYOUT");
                    transaction.commit();

                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return wishListGame.size();
    }


   /* @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.wish_list_row, null);

        coverImage=(ImageView) convertView.findViewById(R.id.wish_game_cover);
        garbage=(ImageView) convertView.findViewById(R.id.garbage);
        gameTitle = (TextView)convertView.findViewById(R.id.username);
        game = getItem(position);
        gameTitle.setText(game.getGameTitle());
        DBQuery dbQuery=new DBQuery();
        Boxart boxart=dbQuery.getBoxArtFromGame(game);
        Glide.with(getContext()).load("http://thegamesdb.net/banners/"+boxart.getThumb()).into(coverImage);
        garbage.setTag(position);
        gameTitle.setTag(position);
        setClickOnGarbage();
        setOnclicOnText();

        return convertView;
    }*/


    /*private void setOnclicOnText(){
        this.gameTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=(int)view.getTag();
                Game tmp=getItem(pos);
                FragmentGameDetail fragmentGameDetail=new FragmentGameDetail();
                Bundle bundle=new Bundle();
                bundle.putInt("GAME ID",tmp.getId());
                fragmentGameDetail.setArguments(bundle);
                boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);

                    //FINAL SOLUTION
                    Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentGameDetail, "GameDetail");
                    transaction.addToBackStack("TABLAYOUT");
                    transaction.commit();
            }
        });
    }*/

}
